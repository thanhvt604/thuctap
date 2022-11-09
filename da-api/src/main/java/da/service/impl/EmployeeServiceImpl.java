package com.globits.da.service.impl;

import com.globits.core.service.impl.GenericServiceImpl;
import com.globits.da.domain.Employee;
import com.globits.da.dto.EmployeeDto;
import com.globits.da.repository.EmployeeRepository;
import com.globits.da.service.EmployeeService;
import com.globits.da.support.Response;
import com.globits.da.support.StatusMessage;
import com.globits.da.support.handle.EmployeeHandle;
import com.globits.da.utils.ImportExportExcelUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private ModelMapper modelMapper;

    @Override
    public Response<EmployeeDto> create(EmployeeDto dto) {
        StatusMessage statusMessage = employeeHandle.employeeValidate(dto, false);

        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }

        Employee employee = employeeHandle.toEntity(null, dto, false);
        employeeRepository.save(employee);
        return new Response<EmployeeDto>(modelMapper.map(employee, EmployeeDto.class), StatusMessage.SUCCESS);

    }

    @Override
    public Response<EmployeeDto> edit(UUID id, EmployeeDto dto) {
        StatusMessage statusMessage = employeeHandle.employeeValidate(dto, true);

        if (StatusMessage.SUCCESS != statusMessage) {
            return new Response<>(statusMessage);
        }

        Employee employee = employeeHandle.toEntity(id, dto, true);
        return new Response<EmployeeDto>(modelMapper.map(employee, EmployeeDto.class), StatusMessage.SUCCESS);
    }

    @Override
    public Response<EmployeeDto> deleteById(UUID id) {
        if (!employeeRepository.existsById(id)) {
            return new Response<>(StatusMessage.EMPLOYEE_ID_NOT_EXISTS);
        }
        EmployeeDto employeeDto = modelMapper.map(employeeRepository.getById(id), EmployeeDto.class);
        employeeRepository.deleteById(id);
        return new Response<EmployeeDto>(employeeDto, StatusMessage.SUCCESS);

    }

    @Override
    public Response<EmployeeDto> getOne(UUID id) {
        if (!employeeRepository.existsById(id)) {
            return new Response<>(StatusMessage.EMPLOYEE_ID_NOT_EXISTS);
        }
        return new Response<EmployeeDto>(modelMapper.map(employeeRepository.getById(id), EmployeeDto.class), StatusMessage.SUCCESS);
    }

    @Override
    public Response<List<EmployeeDto>> exportToExcel(HttpServletResponse response) {
        ImportExportExcelUtil exportExcelUtil = new ImportExportExcelUtil(employeeRepository.findAll());
        return new Response<List<EmployeeDto>>(exportExcelUtil.generateExcelFile(response));
    }

    @Override
    public Response<List<String>> importExcel(MultipartFile multipartFile) {
        return importExportExcelUtil.importExcel(multipartFile);
    }

    public Response<List<EmployeeDto>> getAll() {
        List<EmployeeDto> employeeDtoList;
        employeeDtoList = employeeRepository.findAll().stream().map(employee -> modelMapper.map(employee, EmployeeDto.class)).collect(Collectors.toList());
        if (employeeDtoList.isEmpty()) {
            return new Response<>(StatusMessage.EMPLOYEE_LIST_IS_EMPTY);
        }
        return new Response<List<EmployeeDto>>(employeeDtoList, StatusMessage.SUCCESS);
    }

}
