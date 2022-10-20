package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.Commune;
import com.globits.da.dto.CommuneDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.service.CommuneService;
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
public class CommuneServiceImpl extends GenericServiceImpl<Commune, UUID> implements CommuneService {
    @Autowired
    CommuneRepository communeRepository;
    @Autowired
    CommuneHandle communeHandle;
    @Autowired
    DistrictHandle districtHandle;
    @Autowired
    DistrictRepository districtRepository;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Response<CommuneDto> saveOrUpdate(UUID id, CommuneDto dto) {
        Commune entity = null;
        StatusMessage statusMessage = communeHandle.validateCommune(id, dto);

        if ( StatusMessage.SUCCESS!=statusMessage) {
            return new Response(statusMessage);
        }
        if (id == null && ObjectUtils.isEmpty(dto.getDistrictDto())) {
            return new Response(StatusMessage.DISTRICT_IS_NULL);
        }
        if ((id == null && ObjectUtils.isEmpty(dto.getDistrictDto().getId())) || (id != null && !ObjectUtils.isEmpty(dto.getDistrictDto()) && ObjectUtils.isEmpty(dto.getDistrictDto().getId()))) {
            return new Response(StatusMessage.DISTRICT_ID_IS_NULL);
        }

        if (id == null && !districtRepository.existsById(dto.getDistrictDto().getId()) || (id != null && !ObjectUtils.isEmpty(dto.getDistrictDto())&& !districtRepository.existsById(dto.getDistrictDto().getId()))) {
            return new Response(StatusMessage.DISTRICT_ID_NOT_EXISTS);
        }

        if (id != null) {
            entity = communeRepository.getById(id);
            if(!ObjectUtils.isEmpty(dto.getDistrictDto()))
            {
                entity.setDistrict(districtRepository.getById(dto.getDistrictDto().getId()));
            }
        }

        if (entity == null) {
            entity = new Commune();
        }
        if (id == null) {
            entity.setDistrict(districtRepository.getById(dto.getDistrictDto().getId()));
        }
        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        communeRepository.save(entity);

        return new Response(modelMapper.map(entity,CommuneDto.class), StatusMessage.SUCCESS);

    }

    @Override
    public Response<CommuneDto> deleteById(UUID id) {

        if (!communeRepository.existsById(id)) {
            return new Response(StatusMessage.COMMUNE_ID_IS_NOT_EXIST);

        }

        communeRepository.deleteById(id);
        return new Response(StatusMessage.SUCCESS);
    }

    @Override
    public Response<CommuneDto> getOne(UUID id) {
        if (!communeRepository.existsById(id)) {
            return new Response(StatusMessage.COMMUNE_ID_IS_NOT_EXIST);
        }

        return new Response(modelMapper.map(communeRepository.getById(id), CommuneDto.class), StatusMessage.SUCCESS);
    }

    @Override
    public Response<List<CommuneDto>> getAll() {
        List<CommuneDto> communeDtoList;
        communeDtoList=communeRepository.findAll().stream().map(commune -> modelMapper.map(commune,CommuneDto.class)).collect(Collectors.toList());
        if(communeDtoList.isEmpty())
        {
            return new Response(StatusMessage.COMMUNE_LIST_IS_EMPTY);
        }
        return new Response(communeDtoList,StatusMessage.SUCCESS);
    }


}

