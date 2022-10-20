package com.globits.da.support.ErrorHandle;

import com.globits.da.Constants;
import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.support.StatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
public class EmployeeHandle {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CommuneRepository communeRepository;


    public Boolean existsByEmployeeCode(String code) {
        return employeeRepository.countCode(code) != 0;
    }

    public Employee toEntity(EmployeeDto dto) {
        Employee employee = new Employee();
        employee.setCode(dto.getCode());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setProvinceId(dto.getProvinceId());
        employee.setDistrictId(dto.getDistrictId());
        employee.setCommuneId(dto.getCommuneId());

        return employee;


    }

    public StatusMessage employeeValidate(UUID id, EmployeeDto dto) {
        StatusMessage statusMessage;

        if (ObjectUtils.isEmpty(dto)) {
            return StatusMessage.EMPLOYEE_IS_NULL;
        }

        if (StringUtils.isEmpty(dto.getName())) {
            return StatusMessage.EMPLOYEE_NAME_IS_NULL;
        }

        statusMessage = validateCode(id, dto);
        if (StatusMessage.SUCCESS != statusMessage) {
            return statusMessage;
        }

        statusMessage = validateEmail(dto);
        if (StatusMessage.SUCCESS != statusMessage) {
            return statusMessage;
        }

        statusMessage = validatePhoneNumber(dto);
        if (StatusMessage.SUCCESS != statusMessage) {
            return statusMessage;
        }
        statusMessage = validateAge(dto);

        if (StatusMessage.SUCCESS != statusMessage) {
            return statusMessage;
        }


        if (id == null || !ObjectUtils.isEmpty(dto.getProvinceId())) {
            statusMessage = validateProvinceId(id, dto);
            if (StatusMessage.SUCCESS != statusMessage) {
                return statusMessage;
            }
            statusMessage = validateDistrictId(id, dto);
            if (StatusMessage.SUCCESS != statusMessage) {
                return statusMessage;
            }
            statusMessage = validateCommuneId(id, dto);
            if (StatusMessage.SUCCESS != statusMessage) {
                return statusMessage;
            }

        }

        return StatusMessage.SUCCESS;
    }

    public StatusMessage validateCode(UUID id, EmployeeDto dto) {
        if (StringUtils.isEmpty(dto.getCode())) {
            return StatusMessage.EMPLOYEE_CODE_IS_NULL;
        }
        if ((id == null && existsByEmployeeCode(dto.getCode()) || (id != null && existsByEmployeeCode(dto.getCode()) && !dto.getCode().equals(employeeRepository.getById(id).getCode())))) {

            return StatusMessage.EMPLOYEE_CODE_IS_EXISTS;
        }
        if (!dto.getCode().matches(Constants.REGEX_CODE)) {
            return StatusMessage.WRONG_CODE_FORMAT;
        }
        return StatusMessage.SUCCESS;
    }

    public StatusMessage validateEmail(EmployeeDto dto) {

        if (StringUtils.isEmpty(dto.getEmail())) {
            return StatusMessage.EMPLOYEE_EMAIL_IS_NULL;
        }
        if (!dto.getEmail().matches(Constants.REGEX_EMAIL)) {
            return StatusMessage.WRONG_EMAIL_FORMAT;
        }
        return StatusMessage.SUCCESS;
    }

    public StatusMessage validatePhoneNumber(EmployeeDto dto) {
        if (StringUtils.isEmpty(dto.getPhoneNumber())) {
            return StatusMessage.EMPLOYEE_PHONE_NUMBER_IS_NULL;
        }
        if (!dto.getPhoneNumber().matches(Constants.REGEX_PHONE)) {
            return StatusMessage.WRONG_PHONE_NUMBER_FORMAT;
        }
        return StatusMessage.SUCCESS;
    }

    public StatusMessage validateAge(EmployeeDto dto) {
        if (ObjectUtils.isEmpty(dto.getAge())) {
            return StatusMessage.EMPLOYEE_AGE_IS_NULL;
        }
        if (dto.getAge() < Constants.MIN_AGE) {
            return StatusMessage.AGE_CAN_NOT_NEGATIVE;
        }
        return StatusMessage.SUCCESS;
    }

    public StatusMessage validateProvinceId(UUID id, EmployeeDto dto) {
        if (id == null && ObjectUtils.isEmpty(dto.getProvinceId())) {
            return StatusMessage.ID_PROVINCE_IS_NULL;
        }

        if (!provinceRepository.existsById(dto.getProvinceId())) {
            return StatusMessage.PROVINCE_ID_NOT_EXISTS;
        }
        return StatusMessage.SUCCESS;
    }

    public StatusMessage validateDistrictId(UUID id, EmployeeDto dto) {
        if (id == null && ObjectUtils.isEmpty(dto.getDistrictId())) {
            return StatusMessage.DISTRICT_IS_NULL;
        }
        if (!districtRepository.existsById(dto.getDistrictId())) {
            return StatusMessage.DISTRICT_ID_NOT_EXISTS;
        }
        if (districtRepository.getById(dto.getDistrictId()).getProvince() != provinceRepository.getById(dto.getProvinceId())) {
            return StatusMessage.DISTRICT_OUT_OF_PROVINCE;
        }

        return StatusMessage.SUCCESS;
    }

    public StatusMessage validateCommuneId(UUID id, EmployeeDto dto) {
        if (id == null && ObjectUtils.isEmpty(dto.getCommuneId())) {
            return StatusMessage.COMMUNE_IS_NULL;
        }
        if (!communeRepository.existsById(dto.getCommuneId())) {
            return StatusMessage.COMMUNE_ID_IS_NOT_EXIST;
        }
        if (communeRepository.getById(dto.getCommuneId()).getDistrict() != districtRepository.getById(dto.getDistrictId())) {
            return StatusMessage.COMMUNE_OUT_OF_DISTRICT;
        }
        return StatusMessage.SUCCESS;
    }

}