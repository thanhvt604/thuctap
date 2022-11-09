package com.globits.da.support.handle;

import com.globits.da.Constants;
import com.globits.da.domain.Province;
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
        return provinceRepository.countCode(code) != Constants.NOT_HAS;
    }

    public Boolean existsByProvinceName(String name) {
        return provinceRepository.countName(name) != Constants.NOT_HAS;
    }

    public String getCode(UUID id) {
        return provinceRepository.getById(id).getCode();
    }

    public String getName(UUID id) {
        return provinceRepository.getById(id).getName();
    }

    public Province toEntity(UUID id, ProvinceDto dto, boolean update) {
        Province entity = update ? provinceRepository.getById(id) : new Province();

        entity.setCode(dto.getCode());
        entity.setName(dto.getName());

        if (!ObjectUtils.isEmpty(dto.getDistrictDtoList())) {
            entity.setDistrictList(districtHandle.toEntityList(dto.getDistrictDtoList(), entity, update));
        }
        return entity;
    }


    public StatusMessage validateProvince(UUID id, ProvinceDto dto, boolean update) {
        StatusMessage statusMessage;
        String codeProvinceDto = dto.getCode();
        String nameProvinceDto = dto.getName();

        if (ObjectUtils.isEmpty(dto)) {
            return StatusMessage.PROVINCE_IS_NULL;
        }
        if (update && !provinceRepository.existsById(id)) {
            return StatusMessage.PROVINCE_ID_NOT_EXISTS;
        }
        if (!StringUtils.hasText(nameProvinceDto)) {
            return StatusMessage.NAME_IS_NULL;
        }
        if (!StringUtils.hasText(codeProvinceDto)) {
            return StatusMessage.PROVINCE_CODE_IS_NULL;
        }
        if ((!update && existsByProvinceCode(codeProvinceDto)) || (update && !codeProvinceDto.equals(getCode(id)) && existsByProvinceCode(codeProvinceDto))) {
            return StatusMessage.PROVINCE_CODE_IS_EXIST;
        }
        if ((!update && existsByProvinceName(nameProvinceDto)) || (update && !nameProvinceDto.equals(getName(id)) && existsByProvinceName(nameProvinceDto))) {
            return StatusMessage.PROVINCE_NAME_IS_EXISTS;
        }

        if (!ObjectUtils.isEmpty(dto.getDistrictDtoList())) {
            for (DistrictDto districtDto : dto.getDistrictDtoList()) {
                statusMessage = districtHandle.validateDistrict(districtDto.getId(), districtDto, update);
                if (StatusMessage.SUCCESS != statusMessage) {
                    return statusMessage;
                }
            }
        }
        return StatusMessage.SUCCESS;
    }

}
