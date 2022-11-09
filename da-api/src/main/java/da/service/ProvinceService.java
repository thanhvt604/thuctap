package com.globits.da.service;

import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.support.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ProvinceService extends BaseService<ProvinceDto> {

    Response<List<DistrictDto>> getListDistrict(UUID id);

}
