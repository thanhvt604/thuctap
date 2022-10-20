package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.EmployeeCertificate;
import com.globits.da.dto.EmployeeCertificateDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.EmployeeCertificateRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.service.EmployeeCertificateService;
import com.globits.da.support.ErrorHandle.EmployeeCertificateHandle;
import com.globits.da.support.Response;
import com.globits.da.support.StatusMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeCertificateImpl extends GenericServiceImpl<EmployeeCertificate, UUID> implements EmployeeCertificateService {
    @Autowired
    private EmployeeCertificateHandle employeeCertificateHandle;
    @Autowired
    private EmployeeCertificateRepository employeeCertificateRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public Response<EmployeeCertificateDto> saveOrUpdate(UUID id, EmployeeCertificateDto dto) {
        EmployeeCertificate employeeCertificate = null;
        StatusMessage statusMessage = employeeCertificateHandle.employeeCertificateValidate(dto);
        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response(statusMessage);
        }
        if (id != null) {
            employeeCertificate = employeeCertificateRepository.getById(id);
        }

        if (employeeCertificate == null) {
            employeeCertificate = new EmployeeCertificate();
        }
        employeeCertificate.setEffectiveDate(dto.getEffectiveDate());
        employeeCertificate.setExpirationDate(dto.getExpirationDate());
        employeeCertificate.setProvince(provinceRepository.getById(dto.getProvinceDto().getId()));
        employeeCertificate.setCertificate(certificateRepository.getById(dto.getCertificateDto().getId()));
        employeeCertificate.setEmployee(employeeRepository.getById(dto.getEmployeeDto().getId()));

        employeeCertificateRepository.save(employeeCertificate);
        return new Response(modelMapper.map(employeeCertificate, EmployeeCertificateDto.class), StatusMessage.SUCCESS);


    }

    @Override
    public Response<EmployeeCertificateDto> deleteById(UUID id) {
        if (!employeeCertificateRepository.existsById(id)) {
            return new Response(StatusMessage.EMPLOYEE_HAS_CERTIFICATE_NOT_EXIST);
        }
        EmployeeCertificateDto employeeCertificateDto = modelMapper.map(employeeCertificateRepository.getById(id), EmployeeCertificateDto.class);
        employeeRepository.deleteById(id);
        return new Response(employeeCertificateDto, StatusMessage.SUCCESS);
    }

    @Override
    public Response<EmployeeCertificateDto> getOne(UUID id) {
        if (!employeeCertificateRepository.existsById(id)) {
            return new Response(StatusMessage.EMPLOYEE_HAS_CERTIFICATE_NOT_EXIST);
        }
        return new Response(modelMapper.map(employeeCertificateRepository.getById(id), EmployeeCertificateDto.class), StatusMessage.SUCCESS);
    }

    public Response<List<EmployeeCertificateDto>> getAll() {
        List<EmployeeCertificateDto> employeeCertificateDtoList;
        employeeCertificateDtoList = employeeCertificateRepository.findAll().stream().map(employeeCertificate -> modelMapper.map(employeeCertificate, EmployeeCertificateDto.class)).collect(Collectors.toList());
        if (employeeCertificateDtoList.isEmpty()) {
            return new Response(StatusMessage.NOT_EMPLOYEE_HAS_CERTIFICATE);
        }
        return new Response(employeeCertificateDtoList, StatusMessage.SUCCESS);
    }
}
