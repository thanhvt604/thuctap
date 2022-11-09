package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.service.CertificateService;
import com.globits.da.support.Response;
import com.globits.da.support.StatusMessage;
import com.globits.da.support.handle.CertificateHandle;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl extends GenericServiceImpl<Certificate, UUID> implements CertificateService {
    @Autowired
    private CertificateRepository repos;
    @Autowired
    private CertificateHandle certificateHandle;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Response<CertificateDto> create(CertificateDto dto) {
        StatusMessage statusMessage = certificateHandle.certificateValidate(null, dto, false);
        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }

        Certificate entity = certificateHandle.toEntity(null, dto, false);
        repos.save(entity);
        return new Response<CertificateDto>(modelMapper.map(entity, CertificateDto.class), StatusMessage.SUCCESS);
    }

    @Override
    public Response<CertificateDto> edit(UUID id, CertificateDto dto) {
        StatusMessage statusMessage = certificateHandle.certificateValidate(id, dto, true);
        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }

        Certificate entity = certificateHandle.toEntity(id, dto, true);
        repos.save(entity);
        return new Response<CertificateDto>(modelMapper.map(entity, CertificateDto.class), StatusMessage.SUCCESS);
    }

    @Override
    public Response<CertificateDto> deleteById(UUID id) {

        if (!repos.existsById(id)) {
            return new Response<>(StatusMessage.ID_NOT_EXISTS);
        }
        CertificateDto certificateDto = modelMapper.map(repos.getById(id), CertificateDto.class);
        repos.deleteById(id);
        return new Response<CertificateDto>(certificateDto, StatusMessage.SUCCESS);

    }

    @Override
    public Response<CertificateDto> getOne(UUID id) {
        if (!repos.existsById(id)) {
            return new Response<>(StatusMessage.ID_NOT_EXISTS);
        }
        return new Response<CertificateDto>(modelMapper.map(repos.getById(id), CertificateDto.class), StatusMessage.SUCCESS);
    }

    public Response<List<CertificateDto>> getAll() {
        List<CertificateDto> certificateDtoList;
        certificateDtoList = repos.findAll().stream().map(certificate -> modelMapper.map(certificate, CertificateDto.class)).collect(Collectors.toList());
        if (certificateDtoList.isEmpty()) {
            return new Response<>(StatusMessage.CERTIFICATE_LIST_IS_EMPTY);
        }
        return new Response<List<CertificateDto>>(certificateDtoList, StatusMessage.SUCCESS);
    }
}

