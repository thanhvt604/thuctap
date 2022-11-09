package com.globits.da.rest;


import com.globits.da.dto.EmployeeCertificateDto;
import com.globits.da.service.EmployeeCertificateService;
import com.globits.da.support.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/employeeCertificate")
public class RestEmployeeCertificateController {

    @Autowired
    private EmployeeCertificateService employeeCertificateService;

    @PostMapping("/save")
    public Response<EmployeeCertificateDto> save(@RequestBody EmployeeCertificateDto dto) {
        return employeeCertificateService.create( dto);
    }

    @PutMapping("/{id}")
    public Response<EmployeeCertificateDto> update(@RequestBody EmployeeCertificateDto dto, @PathVariable UUID id) {
        return employeeCertificateService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public Response<EmployeeCertificateDto> deleteById(@PathVariable UUID id) {
        return employeeCertificateService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Response<EmployeeCertificateDto> readById(@PathVariable UUID id) {
        return employeeCertificateService.getOne(id);
    }

    @GetMapping("/list-all")
    public Response<List<EmployeeCertificateDto>> getAll()
    {
        return employeeCertificateService.getAll();
    }

}
