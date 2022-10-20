package com.globits.da.rest;

import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.service.DistrictService;
import com.globits.da.support.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/district")
public class RestDistrictController {

    @Autowired
    private DistrictService districtService;

    @PostMapping("/save")
    public Response<DistrictDto> save(@RequestBody DistrictDto dto) {
        return districtService.saveOrUpdate(null, dto);
    }

    @PutMapping("/{id}")
    public Response<DistrictDto> update(@RequestBody DistrictDto dto, @PathVariable UUID id) {
        return districtService.saveOrUpdate(id, dto);
    }

    @DeleteMapping("/{id}")
    public Response<DistrictDto> deleteById(@PathVariable UUID id) {
        return districtService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Response<DistrictDto> readById(@PathVariable UUID id) {
        return districtService.getOne(id);
    }

    @GetMapping("/communes/{id}")
    public Response<List<CommuneDto>> getCommunesByDistrictId(@PathVariable UUID id) {
        return districtService.getListCommune(id);
    }

    @GetMapping("/list_all")
    public Response<List<DistrictDto>> getAll() {
        return districtService.getAll();
    }
}


