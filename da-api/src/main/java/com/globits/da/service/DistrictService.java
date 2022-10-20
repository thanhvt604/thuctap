package com.globits.da.service;

import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.support.Response;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DistrictService extends BaseService<DistrictDto> {
    Response<List<CommuneDto>> getListCommune(UUID id);

}
