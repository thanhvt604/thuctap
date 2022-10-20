package com.globits.da.rest;

import com.globits.da.dto.CommuneDto;
import com.globits.da.service.CommuneService;
import com.globits.da.support.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/commune")
public class RestCommuneController {

    @Autowired
    private CommuneService communeService;

    @PostMapping("/save")
    public Response<CommuneDto> save(@RequestBody CommuneDto dto) {
        return communeService.saveOrUpdate(null, dto);
    }

    @PutMapping("/{id}")
    public Response<CommuneDto> update(@RequestBody CommuneDto dto, @PathVariable UUID id) {
        return communeService.saveOrUpdate(id, dto);
    }

    @DeleteMapping("{id}")
    public Response<CommuneDto> deleteById(@PathVariable UUID id) {
        return communeService.deleteById(id);
    }

    @GetMapping("{id}")
    public Response<CommuneDto> readById(@PathVariable UUID id) {
        return communeService.getOne(id);
    }

    @GetMapping("/list_all")
    public Response<List<CommuneDto>> getAll()
    {
        return communeService.getAll();
    }
}

