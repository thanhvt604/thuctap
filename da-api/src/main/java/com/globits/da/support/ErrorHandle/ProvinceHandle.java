package com.globits.da.support.ErrorHandle;

import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.support.StatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.UUID;

@Component
public class ProvinceHandle {
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictHandle districtHandle;


    public Boolean existsByProvinceCode(String code) {
        return provinceRepository.countCode(code) != 0;
    }

    public StatusMessage validateProvince(UUID id, ProvinceDto dto) {
        StatusMessage statusMessage;
        if (ObjectUtils.isEmpty(dto)) {
            return StatusMessage.PROVINCE_IS_NULL;
        }
        if (id != null && !provinceRepository.existsById(id)) {
            return StatusMessage.PROVINCE_ID_NOT_EXISTS;
        }
        if ((id == null && existsByProvinceCode(dto.getCode())) || (id != null && !dto.getCode().equals(provinceRepository.getById(id).getCode()) && existsByProvinceCode(dto.getCode()))) {
            return StatusMessage.PROVINCE_CODE_IS_EXIST;
        }
        if (StringUtils.isEmpty(dto.getName())) {
            return StatusMessage.NAME_IS_NULL;
        }
        if (StringUtils.isEmpty(dto.getCode())) {
            return StatusMessage.PROVINCE_CODE_IS_NULL;
        }

        if (dto.getDistrictDtoList() != null) {
            for (DistrictDto districtDto : dto.getDistrictDtoList()) {
                statusMessage = districtHandle.validateDistrict(districtDto.getId(), districtDto);
                if (StatusMessage.SUCCESS != statusMessage) {
                    return statusMessage;
                }
            }
        }
        return StatusMessage.SUCCESS;


    }

}
