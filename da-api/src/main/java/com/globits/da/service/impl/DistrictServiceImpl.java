package com.globits.da.service.impl;
import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.DistrictService;
import com.globits.da.support.ErrorHandle.CommuneHandle;
import com.globits.da.support.ErrorHandle.DistrictHandle;
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
public class DistrictServiceImpl extends GenericServiceImpl<District, UUID> implements DistrictService {
    @Autowired
    private DistrictRepository districtRepos;
    @Autowired
    private ProvinceRepository provinceRepos;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private CommuneHandle communeHandle;
    @Autowired
    private DistrictHandle districtHandle;

    @Override
    public Response<DistrictDto> saveOrUpdate(UUID id, DistrictDto dto) {
        District entity = null;
        StatusMessage statusMessage = districtHandle.validateDistrict(id, dto);
        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response(statusMessage);
        }

        if (id == null && ObjectUtils.isEmpty(dto.getProvinceDto())) {
            return new Response(StatusMessage.PROVINCE_IS_NULL);
        }
        if ((id == null && ObjectUtils.isEmpty(dto.getProvinceDto().getId())) || (id != null && !ObjectUtils.isEmpty(dto.getProvinceDto()) && ObjectUtils.isEmpty(dto.getProvinceDto().getId()))) {
            return new Response(StatusMessage.ID_PROVINCE_IS_NULL);
        }
        if (id == null && !provinceRepos.existsById (dto.getProvinceDto().getId()) || (id != null && !ObjectUtils.isEmpty(dto.getProvinceDto())&& !provinceRepos.existsById(dto.getProvinceDto().getId()))) {
            return new Response(StatusMessage.PROVINCE_ID_NOT_EXISTS);
        }

        if (id != null) {
            entity = districtRepos.getById(id);
            if (!ObjectUtils.isEmpty(dto.getCommuneDtoList())) {
                entity.setCommuneList(communeHandle.updateCommuneList(dto, id));
            }
            if (!ObjectUtils.isEmpty(dto.getProvinceDto())) {
                entity.setProvince(provinceRepos.getById(dto.getProvinceDto().getId()));
            }
        }
        if (entity == null) {
            entity = new District();
        }

        entity.setName(dto.getName());
        entity.setCode(dto.getCode());

        if (id == null) {
            entity.setProvince(provinceRepos.getById(dto.getProvinceDto().getId()));
            District finalEntity = entity;
            if (!ObjectUtils.isEmpty(dto.getCommuneDtoList())) {
                entity.setCommuneList(dto.getCommuneDtoList().stream().map(communeDto -> communeHandle.toEntity(communeDto, finalEntity)).collect(Collectors.toList()));
            }
        }

        districtRepos.save(entity);
        return new Response(new DistrictDto().toDto(entity), StatusMessage.SUCCESS);

    }

    @Override
    public Response<DistrictDto> deleteById(UUID id) {
        if (!districtRepos.existsById(id)) {
            return new Response(StatusMessage.ID_NOT_EXISTS);
        }
        DistrictDto districtDto = modelMapper.map(districtRepos.getById(id), DistrictDto.class);
        districtRepos.deleteById(id);
        return new Response(districtDto, StatusMessage.SUCCESS);

    }

    @Override
    public Response<DistrictDto> getOne(UUID id) {
        if (!districtRepos.existsById(id)) {
            return new Response(StatusMessage.ID_NOT_EXISTS);
        }
        return new Response(modelMapper.map(districtRepos.getById(id), DistrictDto.class), StatusMessage.SUCCESS);
    }

    @Override
    public Response<List<CommuneDto>> getListCommune(UUID id) {
        if (!districtRepos.existsById(id)) {
            return new Response(StatusMessage.ID_NOT_EXISTS);
        }
        if (ObjectUtils.isEmpty(districtRepos.getById(id).getCommuneList())) {
            return new Response(StatusMessage.COMMUNE_LIST_IS_EMPTY);
        }
        DistrictDto districtDto = new DistrictDto();
        List<Commune> communeList = districtRepos.getById(id).getCommuneList();
        districtDto.setCommuneDtoList(communeList.stream().map(commune -> modelMapper.map(commune, CommuneDto.class)).collect(Collectors.toList()));
        return new Response(districtDto.getCommuneDtoList(), StatusMessage.SUCCESS);

    }

    @Override
    public Response<List<DistrictDto>> getAll() {
        List<DistrictDto> districtDtoList;
        districtDtoList = districtRepos.findAll().stream().map(district -> modelMapper.map(district, DistrictDto.class)).collect(Collectors.toList());
        if (districtDtoList.isEmpty()) {
            return new Response(StatusMessage.DISTRICT_LIST_IS_EMPTY);
        }
        return new Response(districtDtoList, StatusMessage.SUCCESS);
    }

}


