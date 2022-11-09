package com.globits.da.support.handle;

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
        return employeeRepository.countCode(code) != Constants.NOT_HAS;
    }

    public String getCode(UUID id) {
        return employeeRepository.getById(id).getCode();
    }

    public Employee toEntity(UUID id, EmployeeDto dto, boolean update) {
        Employee employee = update ? employeeRepository.getById(id) : new Employee();
        employee.setCode(dto.getCode());
        employee.setAge(dto.getAge());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        if (!update || !ObjectUtils.isEmpty(dto.getProvinceDto())) {
            employee.setProvince(provinceRepository.getById(dto.getProvinceDto().getId()));
            employee.setDistrict(districtRepository.getById(dto.getDistrictDto().getId()));
            employee.setCommune(communeRepository.getById(dto.getCommuneDto().getId()));
        }
        return employee;


    }

    public StatusMessage employeeValidate(EmployeeDto dto, boolean update) {
        StatusMessage statusMessage;

        if (ObjectUtils.isEmpty(dto)) {
            return StatusMessage.EMPLOYEE_IS_NULL;
        }

        if (!StringUtils.hasText(dto.getName())) {
            return StatusMessage.EMPLOYEE_NAME_IS_NULL;
        }

        statusMessage = validateCode(dto, update);
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

        if (!update || !ObjectUtils.isEmpty(dto.getProvinceDto())) {
            statusMessage = validateProvinceId(dto);
            if (StatusMessage.SUCCESS != statusMessage) {
                return statusMessage;
            }
            statusMessage = validateDistrictId(dto);
            if (StatusMessage.SUCCESS != statusMessage) {
                return statusMessage;
            }
            return validateCommuneId(dto);
        }

        return StatusMessage.SUCCESS;
    }

    public StatusMessage validateCode(EmployeeDto dto, boolean update) {
        String employeeCodeDto = dto.getCode();
        if (!StringUtils.hasText(employeeCodeDto)) {
            return StatusMessage.EMPLOYEE_CODE_IS_NULL;
        }

        if ((!update && existsByEmployeeCode(employeeCodeDto) || (update &&
                existsByEmployeeCode(employeeCodeDto) && !dto.getCode().equals(employeeCodeDto)))) {
            return StatusMessage.EMPLOYEE_CODE_IS_EXISTS;
        }

        if (!employeeCodeDto.matches(Constants.REGEX_CODE)) {
            return StatusMessage.WRONG_CODE_FORMAT;
        }
        return StatusMessage.SUCCESS;
    }

    public StatusMessage validateEmail(EmployeeDto dto) {

        if (!StringUtils.hasText(dto.getEmail())) {
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

    public StatusMessage validateProvinceId(EmployeeDto dto) {
        UUID provinceIdDto = dto.getProvinceDto().getId();
        if (ObjectUtils.isEmpty(dto.getProvinceDto())) {
            return StatusMessage.PROVINCE_IS_NULL;
        }
        if (ObjectUtils.isEmpty(provinceIdDto)) {
            return StatusMessage.ID_PROVINCE_IS_NULL;
        }

        if (!provinceRepository.existsById(provinceIdDto)) {
            return StatusMessage.PROVINCE_ID_NOT_EXISTS;
        }
        return StatusMessage.SUCCESS;
    }

    public StatusMessage validateDistrictId(EmployeeDto dto) {
        UUID districtIdDto = dto.getDistrictDto().getId();
        if (ObjectUtils.isEmpty(dto.getDistrictDto())) {
            return StatusMessage.DISTRICT_IS_NULL;
        }
        if (ObjectUtils.isEmpty(districtIdDto)) {
            return StatusMessage.DISTRICT_ID_IS_NULL;
        }
        if (!districtRepository.existsById(districtIdDto)) {
            return StatusMessage.DISTRICT_ID_NOT_EXISTS;
        }
        if (districtRepository.getById(districtIdDto).getProvince() != provinceRepository.getById(dto.getProvinceDto().getId())) {
            return StatusMessage.DISTRICT_OUT_OF_PROVINCE;
        }

        return StatusMessage.SUCCESS;
    }

    public StatusMessage validateCommuneId(EmployeeDto dto) {
        UUID communeIdDto = dto.getCommuneDto().getId();

        if (ObjectUtils.isEmpty(dto.getCommuneDto())) {
            return StatusMessage.COMMUNE_IS_NULL;
        }

        if (ObjectUtils.isEmpty(communeIdDto)) {
            return StatusMessage.COMMUNE_ID_IS_NOT_EXIST;
        }
        if (!communeRepository.existsById(communeIdDto)) {
            return StatusMessage.COMMUNE_ID_IS_NOT_EXIST;
        }
        if (communeRepository.getById(communeIdDto).getDistrict() != districtRepository.getById(dto.getDistrictDto().getId())) {
            return StatusMessage.COMMUNE_OUT_OF_DISTRICT;
        }
        return StatusMessage.SUCCESS;
    }
}