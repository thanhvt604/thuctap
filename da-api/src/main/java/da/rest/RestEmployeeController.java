package com.globits.da.rest;

import com.globits.da.dto.EmployeeDto;
import com.globits.da.service.EmployeeService;
import com.globits.da.support.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employee")
public class RestEmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/save")
    public Response<EmployeeDto> save(@RequestBody EmployeeDto dto) {
        return employeeService.create(dto);
    }

    @PutMapping("/{id}")
    public Response<EmployeeDto> update(@RequestBody EmployeeDto dto, @PathVariable UUID id) {
        return employeeService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public Response<EmployeeDto> deleteById(@PathVariable UUID id) {
        return employeeService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Response<EmployeeDto> readById(@PathVariable UUID id) {
        return employeeService.getOne(id);
    }

    @GetMapping("/list-all")
    public Response<List<EmployeeDto>> getAll() {
        return employeeService.getAll();
    }

    @PostMapping("/export-to-excel")
    public Response<List<EmployeeDto>> exportToExcel(HttpServletResponse response) {

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=employees.xlsx";
        response.setHeader(headerKey, headerValue);
        return employeeService.exportToExcel(response);
    }

    @PostMapping("/import-from-excel")
    public Response<List<String>> importFromExcel(@RequestParam("file") MultipartFile file) {
        return employeeService.importExcel(file);
    }
}