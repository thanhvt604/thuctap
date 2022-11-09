package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.ProvinceService;
import com.globits.da.support.Response;
import com.globits.da.support.StatusMessage;
import com.globits.da.support.handle.ProvinceHandle;
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
    private ProvinceHandle provinceHandle;


    @Override
    public Response<ProvinceDto> create(ProvinceDto dto) {

        StatusMessage statusMessage = provinceHandle.validateProvince(null, dto, false);
        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }

        Province entity = provinceHandle.toEntity(null, dto, false);
        repos.save(entity);
        return new Response<ProvinceDto>(ProvinceDto.toDto(entity), StatusMessage.SUCCESS);
    }

    @Override
    public Response<ProvinceDto> edit(UUID id, ProvinceDto dto) {

        StatusMessage statusMessage = provinceHandle.validateProvince(id, dto, true);
        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }

        Province entity = provinceHandle.toEntity(id, dto, true);
        repos.save(entity);
        return new Response<ProvinceDto>(ProvinceDto.toDto(entity), StatusMessage.SUCCESS);


    }

    @Override
    public Response<ProvinceDto> deleteById(UUID id) {
        ProvinceDto provinceDto;

        if (!repos.existsById(id)) {
            return new Response<>(StatusMessage.ID_NOT_EXISTS);
        }

        provinceDto = ProvinceDto.toDto(repos.getById(id));
        repos.deleteById(id);
        return new Response<ProvinceDto>(provinceDto, StatusMessage.SUCCESS);
    }

    @Override
    public Response<ProvinceDto> getOne(UUID id) {

        if (!repos.existsById(id)) {
            return new Response<>(StatusMessage.ID_NOT_EXISTS);
        }
        return new Response<ProvinceDto>(modelMapper.map(repos.getById(id), ProvinceDto.class), StatusMessage.SUCCESS);
    }


    @Override
    public Response<List<DistrictDto>> getListDistrict(UUID id) {
        if (!repos.existsById(id)) {
            return new Response<>(StatusMessage.PROVINCE_ID_NOT_EXISTS);
        }

        if (ObjectUtils.isEmpty(repos.getById(id).getDistrictList())) {
            return new Response<>(StatusMessage.DISTRICT_LIST_IS_NULL);
        }

        ProvinceDto provinceDto = new ProvinceDto();
        List<District> districts = repos.getById(id).getDistrictList();
        provinceDto.setDistrictDtoList(districts.stream().map((District district) -> modelMapper.map(district, DistrictDto.class)).collect(Collectors.toList()));
        return new Response<List<DistrictDto>>(provinceDto.getDistrictDtoList(), StatusMessage.SUCCESS);
    }

    @Override
    public Response<List<ProvinceDto>> getAll() {

        List<ProvinceDto> provinceDtoList;
        provinceDtoList = repos.findAll().stream().map(province -> modelMapper.map(province, ProvinceDto.class)).collect(Collectors.toList());
        if (provinceDtoList.isEmpty()) {
            return new Response<>(StatusMessage.PROVINCE_LIST_IS_EMPTY);
        }
        return new Response<List<ProvinceDto>>(provinceDtoList, StatusMessage.SUCCESS);
    }

}

