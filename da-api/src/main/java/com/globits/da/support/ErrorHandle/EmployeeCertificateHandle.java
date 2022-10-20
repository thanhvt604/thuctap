package com.globits.da.support.ErrorHandle;

import com.globits.da.Constants;
import com.globits.da.dto.CertificateDto;
import com.globits.da.dto.EmployeeCertificateDto;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.repository.EmployeeCertificateRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.support.StatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

@Component
public class EmployeeCertificateHandle {
    @Autowired
    private EmployeeCertificateRepository employeeCertificateRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private CertificateRepository certificateRepository;

    public Boolean hasCertificateOfProvince(String employeeId, String certificateId, String provinceId, LocalDate now) {
        return employeeCertificateRepository.hasCertificateOfProvince(employeeId, certificateId, provinceId, now) > Constants.NOT_HAS;
    }

    public Boolean hasThreeCertificateSameType(String certificateId, LocalDate now) {
        return employeeCertificateRepository.hasThreeCertificateSameType(certificateId, now) >= Constants.MAX_SAME_TYPE_CERTIFICATE;
    }

    public StatusMessage employeeCertificateValidate(EmployeeCertificateDto dto) {
        StatusMessage statusMessage = validateDate(dto.getEffectiveDate(), dto.getExpirationDate());
        if (StatusMessage.SUCCESS != statusMessage) {
            return statusMessage;
        }

        statusMessage = validateProvince(dto.getProvinceDto());
        if (StatusMessage.SUCCESS != statusMessage) {
            return statusMessage;
        }
        statusMessage = validateEmployee(dto.getEmployeeDto());
        if (StatusMessage.SUCCESS != statusMessage) {
            return statusMessage;
        }

        statusMessage = validateCertificate(dto.getCertificateDto());
        if (StatusMessage.SUCCESS != statusMessage) {
            return statusMessage;
        }

        statusMessage = considerTheConditionsToAdd(dto);
        if (StatusMessage.SUCCESS != statusMessage) {
            return statusMessage;
        }

        return StatusMessage.SUCCESS;
    }

    public StatusMessage validateDate(LocalDate effectiveDate, LocalDate expirationDate) {
        if (ObjectUtils.isEmpty(effectiveDate)) {
            return StatusMessage.EFFECTIVE_DATE_IS_NULL;
        }
        if (ObjectUtils.isEmpty(expirationDate)) {
            return StatusMessage.EXPIRATION_DATE_IS_NULL;
        }
        if (effectiveDate.isAfter(expirationDate)) {
            return StatusMessage.WRONG_POSITION_FOR_DATE;
        }
        LocalDate now = LocalDate.now();
        if (expirationDate.isBefore(now)) {
            return StatusMessage.EXPIRATION_IS_BEFORE_NOW;
        }
        return StatusMessage.SUCCESS;

    }

    public StatusMessage validateProvince(ProvinceDto dto) {
        if (ObjectUtils.isEmpty(dto)) {
            return StatusMessage.PROVINCE_IS_NULL;
        }
        if (ObjectUtils.isEmpty(dto.getId())) {
            return StatusMessage.ID_PROVINCE_IS_NULL;
        }
        if (!provinceRepository.existsById(dto.getId())) {
            return StatusMessage.PROVINCE_ID_NOT_EXISTS;
        }
        return StatusMessage.SUCCESS;

    }

    public StatusMessage validateEmployee(EmployeeDto dto) {
        if (ObjectUtils.isEmpty(dto)) {
            return StatusMessage.EMPLOYEE_IS_NULL;
        }
        if (ObjectUtils.isEmpty(dto.getId())) {
            return StatusMessage.EMPLOYEE_ID_IS_NULL;
        }
        if (!employeeRepository.existsById(dto.getId())) {
            return StatusMessage.EMPLOYEE_ID_NOT_EXISTS;
        }
        return StatusMessage.SUCCESS;

    }

    public StatusMessage validateCertificate(CertificateDto dto) {
        if (ObjectUtils.isEmpty(dto)) {
            return StatusMessage.CERTIFICATE_IS_NULL;
        }
        if (ObjectUtils.isEmpty(dto.getId())) {
            return StatusMessage.CERTIFICATE_ID_IS_NULL;
        }
        if (!certificateRepository.existsById(dto.getId())) {
            return StatusMessage.CERTIFICATE_ID_NOT_EXISTS;
        }
        return StatusMessage.SUCCESS;

    }

    public StatusMessage considerTheConditionsToAdd(EmployeeCertificateDto dto) {
        LocalDate now = LocalDate.now();
        String employeeId = dto.getEmployeeDto().getId().toString();
        String provinceId = dto.getProvinceDto().getId().toString();
        String certificateId = dto.getCertificateDto().getId().toString();

        if (hasCertificateOfProvince(employeeId, certificateId, provinceId, now)) {
            return StatusMessage.EMPLOYEE_HAS_CERTIFICATE_OF_PROVINCE;
        }

        if (hasThreeCertificateSameType(certificateId, now)) {
            return StatusMessage.EMPLOYEE_HAS_THREE_CERTIFICATE_SAME_TYPE;
        }
        return StatusMessage.SUCCESS;


    }


}
