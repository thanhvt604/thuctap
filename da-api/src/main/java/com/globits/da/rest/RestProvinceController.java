package com.globits.da.rest;

import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.service.ProvinceService;
import com.globits.da.support.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/province")
public class RestProvinceController {

    @Autowired
    ProvinceService provinceService;

    @PostMapping("/save")
    public Response<ProvinceDto> save(@RequestBody ProvinceDto dto) {
        return provinceService.saveOrUpdate(null, dto);

    }

    @PutMapping("/{id}")
    public Response<ProvinceDto> update(@RequestBody ProvinceDto dto, @PathVariable UUID id) {
        return provinceService.saveOrUpdate(id, dto);
    }

    @DeleteMapping("/{id}")
    public Response<ProvinceDto> deleteById(@PathVariable UUID id) {
        return provinceService.deleteById(id);
    }

    @GetMapping("/{id}")
    public Response<ProvinceDto> readById(@PathVariable UUID id) {
        return provinceService.getOne(id);
    }

    @GetMapping("/districts/{id}")
    public Response<List<DistrictDto>> getListByProvinceId(@PathVariable UUID id) {
        return provinceService.getListDistrict(id);
    }

    @GetMapping("/list_all")
    public Response<List<ProvinceDto>> getAll() {
        return provinceService.getAll();
    }

}
