package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.dto.CommuneDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.service.CommuneService;
import com.globits.da.support.Response;
import com.globits.da.support.StatusMessage;
import com.globits.da.support.handle.CommuneHandle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CommuneServiceImpl extends GenericServiceImpl<Commune, UUID> implements CommuneService {
    @Autowired
    private CommuneRepository communeRepository;
    @Autowired
    private CommuneHandle communeHandle;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Response<CommuneDto> create(CommuneDto dto) {
        StatusMessage statusMessage = communeHandle.validateCommune(null, dto, false);

        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }
        statusMessage = communeHandle.setDistrictValidate(dto);
        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }
        District district = null;
        if (!ObjectUtils.isEmpty(dto.getDistrictDto())) {
            district = districtRepository.getById(dto.getDistrictDto().getId());
        }

        Commune entity = communeHandle.toCommuneEntity(null, dto, district, false);
        communeRepository.save(entity);
        return new Response<CommuneDto>(modelMapper.map(entity, CommuneDto.class), StatusMessage.SUCCESS);
    }

    @Override
    public Response<CommuneDto> edit(UUID id, CommuneDto dto) {
        StatusMessage statusMessage = communeHandle.validateCommune(id, dto, true);

        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }
        statusMessage = communeHandle.setDistrictValidate(dto);

        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }

        District district = null;
        if (!ObjectUtils.isEmpty(dto.getDistrictDto())) {
            district = districtRepository.getById(dto.getDistrictDto().getId());
        }
        Commune entity = communeHandle.toCommuneEntity(id, dto, district, true);
        communeRepository.save(entity);
        return new Response<CommuneDto>(modelMapper.map(entity, CommuneDto.class), StatusMessage.SUCCESS);


    }

    @Override
    public Response<CommuneDto> deleteById(UUID id) {

        if (!communeRepository.existsById(id)) {
            return new Response<>(StatusMessage.COMMUNE_ID_IS_NOT_EXIST);

        }
        communeRepository.deleteById(id);
        return new Response<CommuneDto>(StatusMessage.SUCCESS);
    }

    @Override
    public Response<CommuneDto> getOne(UUID id) {
        if (!communeRepository.existsById(id)) {
            return new Response<>(StatusMessage.COMMUNE_ID_IS_NOT_EXIST);
        }

        return new Response<CommuneDto>(modelMapper.map(communeRepository.getById(id), CommuneDto.class), StatusMessage.SUCCESS);
    }

    @Override
    public Response<List<CommuneDto>> getAll() {
        List<CommuneDto> communeDtoList;
        communeDtoList = communeRepository.findAll().stream().map(commune -> modelMapper.map(commune, CommuneDto.class)).collect(Collectors.toList());
        if (communeDtoList.isEmpty()) {
            return new Response<>(StatusMessage.COMMUNE_LIST_IS_EMPTY);
        }
        return new Response<List<CommuneDto>>(communeDtoList, StatusMessage.SUCCESS);
    }


}

