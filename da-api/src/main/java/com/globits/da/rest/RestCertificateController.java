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

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Response<CertificateDto> save(@RequestBody CertificateDto dto) {
        return certificateService.saveOrUpdate(null, dto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Response<CertificateDto> update(@RequestBody CertificateDto dto, @PathVariable UUID id) {
        return certificateService.saveOrUpdate(id, dto);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Response<CertificateDto> deleteById(@PathVariable UUID id) {
        return certificateService.deleteById(id);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<CertificateDto> readById(@PathVariable UUID id) {
        return certificateService.getOne(id);
    }

    @GetMapping("/list_all")
    public Response<List<CertificateDto>> getAll()
    {
        return certificateService.getAll();
    }
}
