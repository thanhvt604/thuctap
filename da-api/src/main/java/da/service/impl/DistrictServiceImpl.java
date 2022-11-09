package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.DistrictService;
import com.globits.da.support.Response;
import com.globits.da.support.StatusMessage;
import com.globits.da.support.handle.DistrictHandle;
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
    private ModelMapper modelMapper;
    @Autowired
    private DistrictHandle districtHandle;
    @Autowired
    private ProvinceRepository provinceRepository;

    @Override
    public Response<DistrictDto> create(DistrictDto dto) {
        StatusMessage statusMessage = districtHandle.validateDistrict(null, dto, false);
        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }

        statusMessage = districtHandle.validateSetProvince(dto);
        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }

        Province province = null;
        if (!ObjectUtils.isEmpty(dto.getProvinceDto())) {
            province = provinceRepository.getById(dto.getProvinceDto().getId());
        }

        District entity = districtHandle.toDistrictEntity(null, dto, province, false);
        districtRepos.save(entity);
        return new Response<DistrictDto>(DistrictDto.toDto(entity), StatusMessage.SUCCESS);
    }

    @Override
    public Response<DistrictDto> edit(UUID id, DistrictDto dto) {
        StatusMessage statusMessage = districtHandle.validateDistrict(id, dto, true);
        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }
        statusMessage = districtHandle.validateSetProvince(dto);
        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }

        Province province = null;
        if (!ObjectUtils.isEmpty(dto.getProvinceDto())) {
            province = provinceRepository.getById(dto.getProvinceDto().getId());
        }

        District entity = districtHandle.toDistrictEntity(id, dto, province, true);
        districtRepos.save(entity);
        return new Response<DistrictDto>(DistrictDto.toDto(entity), StatusMessage.SUCCESS);
    }

    @Override
    public Response<DistrictDto> deleteById(UUID id) {
        if (!districtRepos.existsById(id)) {
            return new Response<>(StatusMessage.ID_NOT_EXISTS);
        }
        DistrictDto districtDto = modelMapper.map(districtRepos.getById(id), DistrictDto.class);
        districtRepos.deleteById(id);
        return new Response<DistrictDto>(districtDto, StatusMessage.SUCCESS);

    }

    @Override
    public Response<DistrictDto> getOne(UUID id) {
        if (!districtRepos.existsById(id)) {
            return new Response<>(StatusMessage.ID_NOT_EXISTS);
        }
        return new Response<DistrictDto>(modelMapper.map(districtRepos.getById(id), DistrictDto.class), StatusMessage.SUCCESS);
    }

    @Override
    public Response<List<CommuneDto>> getListCommune(UUID id) {
        if (!districtRepos.existsById(id)) {
            return new Response<>(StatusMessage.ID_NOT_EXISTS);
        }
        if (ObjectUtils.isEmpty(districtRepos.getById(id).getCommuneList())) {
            return new Response<>(StatusMessage.COMMUNE_LIST_IS_EMPTY);
        }
        DistrictDto districtDto = new DistrictDto();
        List<Commune> communeList = districtRepos.getById(id).getCommuneList();
        districtDto.setCommuneDtoList(communeList.stream().map(commune -> modelMapper.map(commune, CommuneDto.class)).collect(Collectors.toList()));
        return new Response<List<CommuneDto>>(districtDto.getCommuneDtoList(), StatusMessage.SUCCESS);

    }

    @Override
    public Response<List<DistrictDto>> getAll() {
        List<DistrictDto> districtDtoList;
        districtDtoList = districtRepos.findAll().stream().map(district -> modelMapper.map(district, DistrictDto.class)).collect(Collectors.toList());
        if (districtDtoList.isEmpty()) {
            return new Response<>(StatusMessage.DISTRICT_LIST_IS_EMPTY);
        }
        return new Response<List<DistrictDto>>(districtDtoList, StatusMessage.SUCCESS);
    }

}


