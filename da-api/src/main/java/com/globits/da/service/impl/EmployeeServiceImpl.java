package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.support.ErrorHandle.EmployeeHandle;
import com.globits.da.support.Response;
import com.globits.da.support.StatusMessage;
import com.globits.da.utils.ImportExportExcelUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl extends GenericServiceImpl<Employee, UUID> implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private EmployeeHandle employeeHandle;
    @Autowired
    private ImportExportExcelUtil importExportExcelUtil;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public Response<EmployeeDto> saveOrUpdate(UUID id, EmployeeDto dto) {
        Employee employee = null;

        StatusMessage statusMessage = employeeHandle.employeeValidate(id, dto);
        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response(statusMessage);
        }

        if (id != null) {
            employee = employeeRepository.getById(id);
        }

        if (employee == null) {
            employee = new Employee();
        }
        employee.setName(dto.getName());
        employee.setCode(dto.getCode());
        employee.setAge(dto.getAge());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setEmail(dto.getEmail());

        if (id == null || (id != null && !ObjectUtils.isEmpty(dto.getProvinceId()))) {
            employee.setProvinceId(dto.getProvinceId());
            employee.setDistrictId(dto.getDistrictId());
            employee.setCommuneId(dto.getCommuneId());
        }

        employeeRepository.save(employee);
        return new Response(modelMapper.map(employee, EmployeeDto.class), StatusMessage.SUCCESS);

    }

    @Override
    public Response<EmployeeDto> deleteById(UUID id) {
        if (!employeeRepository.existsById(id)) {
            return new Response(StatusMessage.EMPLOYEE_ID_NOT_EXISTS);
        }
        EmployeeDto employeeDto = modelMapper.map(employeeRepository.getById(id), EmployeeDto.class);
        employeeRepository.deleteById(id);
        return new Response(employeeDto, StatusMessage.SUCCESS);

    }

    @Override
    public Response<EmployeeDto> getOne(UUID id) {
        if (!employeeRepository.existsById(id)) {
            return new Response(StatusMessage.EMPLOYEE_ID_NOT_EXISTS);
        }
        return new Response(modelMapper.map(employeeRepository.getById(id), EmployeeDto.class), StatusMessage.SUCCESS);
    }

    @Override
    public Response<?> exportToExcel(HttpServletResponse response) {
        ImportExportExcelUtil exportExcelUtil = new ImportExportExcelUtil(employeeRepository.findAll());
        return new Response(exportExcelUtil.generateExcelFile(response));
    }

    @Override
    public Response<List<String>> importExcel(MultipartFile multipartFile) {
        return importExportExcelUtil.importExcel(multipartFile);
    }

    public Response<List<EmployeeDto>> getAll() {
        List<EmployeeDto> employeeDtoList;
        employeeDtoList = employeeRepository.findAll().stream().map(employee -> modelMapper.map(employee, EmployeeDto.class)).collect(Collectors.toList());
        if (employeeDtoList.isEmpty()) {
            return new Response(StatusMessage.EMPLOYEE_LIST_IS_EMPTY);
        }
        return new Response(employeeDtoList, StatusMessage.SUCCESS);
    }

}
