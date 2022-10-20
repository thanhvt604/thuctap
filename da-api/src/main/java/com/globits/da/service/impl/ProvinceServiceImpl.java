package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.ProvinceService;
import com.globits.da.support.ErrorHandle.DistrictHandle;
import com.globits.da.support.ErrorHandle.ProvinceHandle;
import com.globits.da.support.Response;
import com.globits.da.support.StatusMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProvinceServiceImpl extends GenericServiceImpl<Province, UUID> implements ProvinceService {
    @Autowired
    private ProvinceRepository repos;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private DistrictHandle districtHandle;
    @Autowired
    private ProvinceHandle provinceHandle;

    @Override
    public Response<ProvinceDto> saveOrUpdate(UUID id, ProvinceDto dto) {
        Province entity = null;

        StatusMessage statusMessage = provinceHandle.validateProvince(id, dto);
        if (StatusMessage.SUCCESS!=statusMessage) {
            return new Response(statusMessage);
        }
        if (id != null) {
            entity = repos.getById(id);
            if (!ObjectUtils.isEmpty(dto.getDistrictDtoList())) {
                entity.setDistrictList(districtHandle.updateDistrictList(dto));
            }
        }
        if (entity == null) {
            entity = new Province();
        }

        entity.setName(dto.getName());
        entity.setCode(dto.getCode());

        if (id == null && !ObjectUtils.isEmpty(dto.getDistrictDtoList())) {
            entity.setDistrictList(districtHandle.toEntityList(dto.getDistrictDtoList(), entity));
        }
        repos.save(entity);
        ProvinceDto provinceDto =new ProvinceDto();
        return new Response(provinceDto.toDto(entity),StatusMessage.SUCCESS);

    }

    @Override
    public Response<ProvinceDto> deleteById(UUID id) {
        ProvinceDto provinceDto=new ProvinceDto();

        if (!repos.existsById(id)) {
            return new Response(StatusMessage.ID_NOT_EXISTS);
        }
        provinceDto= provinceDto.toDto(repos.getById(id));
        repos.deleteById(id);
        return new Response(provinceDto,StatusMessage.SUCCESS);
    }

    @Override
    public Response<ProvinceDto> getOne(UUID id) {
        if (!repos.existsById(id)) {
            return new Response(StatusMessage.ID_NOT_EXISTS);

        }
        return new Response(modelMapper.map(repos.getById(id), ProvinceDto.class), StatusMessage.SUCCESS);
    }



    @Override
    public Response<List<DistrictDto>> getListDistrict(UUID id) {
        if(!repos.existsById(id))
        {
            return new Response(StatusMessage.PROVINCE_ID_NOT_EXISTS);
        }

        if (ObjectUtils.isEmpty(repos.getById(id).getDistrictList())) {
            return new Response(StatusMessage.DISTRICT_LIST_IS_NULL);
        }
        ProvinceDto provinceDto = new ProvinceDto();
        List<District> districts = repos.getById(id).getDistrictList();
        provinceDto.setDistrictDtoList(districts.stream().map((District district) -> modelMapper.map(district, DistrictDto.class)).collect(Collectors.toList()));
        return new Response(provinceDto.getDistrictDtoList(), StatusMessage.SUCCESS);
    }

    @Override
    public Response<List<ProvinceDto>> getAll() {
       List<ProvinceDto> provinceDtoList;
       provinceDtoList=repos.findAll().stream().map(province -> modelMapper.map(province,ProvinceDto.class)).collect(Collectors.toList());
       if(provinceDtoList.isEmpty())
       {
           return new Response(StatusMessage.PROVINCE_LIST_IS_EMPTY);
       }
       return new Response(provinceDtoList,StatusMessage.SUCCESS);
    }

}

