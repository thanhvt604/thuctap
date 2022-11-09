package com.globits.da.rest;

import com.globits.da.dto.CertificateDto;
import com.globits.da.service.CertificateService;
import com.globits.da.support.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/certificate")
public class RestCertificateController {

    @Autowired
    private CertificateService certificateService;

    @PostMapping("/save")
    public Response<CertificateDto> save(@RequestBody CertificateDto dto) {
        return certificateService.create(dto);
    }

    @PutMapping(value = "/{id}")
    public Response<CertificateDto> update(@RequestBody CertificateDto dto, @PathVariable UUID id) {
        return certificateService.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public Response<CertificateDto> deleteById(@PathVariable UUID id) {
        return certificateService.deleteById(id);
    }

    @GetMapping(value = "/{id}")
    public Response<CertificateDto> readById(@PathVariable UUID id) {
        return certificateService.getOne(id);
    }

    @GetMapping("/list-all")
    public Response<List<CertificateDto>> getAll() {
        return certificateService.getAll();
    }
}
