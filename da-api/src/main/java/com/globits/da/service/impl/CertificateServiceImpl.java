package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.Certificate;
import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.service.CertificateService;
import com.globits.da.support.ErrorHandle.CertificateHandle;
import com.globits.da.support.Response;
import com.globits.da.support.StatusMessage;
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
    public Response<CertificateDto> saveOrUpdate(UUID id, CertificateDto dto) {
        Certificate entity = null;

        StatusMessage statusMessage = certificateHandle.certificateValidate(id, dto);
        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response(statusMessage);
        }
        if (id != null) {
            entity = repos.getById(id);
        }

        if (entity == null) {
            entity = new Certificate();
        }

        entity.setName(dto.getName());
        entity.setCode(dto.getCode());
        entity.setEffectiveDate(dto.getEffectiveDate());
        entity.setExpirationDate(dto.getExpirationDate());

        repos.save(entity);
        return new Response(modelMapper.map(entity,CertificateDto.class), StatusMessage.SUCCESS);

    }

    @Override
    public Response<CertificateDto> deleteById(UUID id) {

        if (!repos.existsById(id)) {
            return new Response(StatusMessage.ID_NOT_EXISTS);
        }
        CertificateDto certificateDto=modelMapper.map(repos.getById(id),CertificateDto.class);
        repos.deleteById(id);
        return new Response(certificateDto, StatusMessage.SUCCESS);

    }

    @Override
    public Response<CertificateDto> getOne(UUID id) {
        if (!repos.existsById(id)) {
            return new Response(StatusMessage.ID_NOT_EXISTS);
        }
        return new Response(modelMapper.map(repos.getById(id), CertificateDto.class), StatusMessage.SUCCESS);
    }

    public Response<List<CertificateDto>> getAll() {
        List<CertificateDto> certificateDtoList;
        certificateDtoList=repos.findAll().stream().map(certificate -> modelMapper.map(certificate,CertificateDto.class)).collect(Collectors.toList());
        if(certificateDtoList.isEmpty())
        {
            return new Response(StatusMessage.CERTIFICATE_LIST_IS_EMPTY);
        }
        return new Response(certificateDtoList,StatusMessage.SUCCESS);
    }
}

