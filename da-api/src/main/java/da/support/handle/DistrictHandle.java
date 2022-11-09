package com.globits.da.support.handle;

import com.globits.da.Constants;
import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.repository.ProvinceRepository;
import com.globits.da.support.StatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DistrictHandle {
    @Autowired
    CommuneHandle communeHandle;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ProvinceRepository provinceRepository;


    public Boolean existsByDistrictCode(String code) {
        return districtRepository.countCode(code) != Constants.NOT_HAS;
    }

    public Boolean existsByDistrictName(String name) {
        return districtRepository.countName(name) != Constants.NOT_HAS;
    }

    public String getCode(UUID id) {
        return districtRepository.getById(id).getCode();
    }

    public String getName(UUID id) {
        return districtRepository.getById(id).getName();
    }

    public List<District> toEntityList(List<DistrictDto> dtoList, Province province, boolean update) {
        return dtoList.stream().map(districtDto -> toDistrictEntity(districtDto.getId(), districtDto, province, update)).collect(Collectors.toList());

    }

    public District toDistrictEntity(UUID id, DistrictDto districtDto, Province province, boolean update) {

        District district = update ? districtRepository.getById(id) : new District();

        district.setCode(districtDto.getCode());
        district.setName(districtDto.getName());

        if (!ObjectUtils.isEmpty(districtDto.getCommuneDtoList())) {
            district.setCommuneList(communeHandle.toEntityList(districtDto.getCommuneDtoList(), district, update));
        }
        if (!ObjectUtils.isEmpty(province)) {
            district.setProvince(province);
        }

        return district;
    }

    public StatusMessage validateDistrict(UUID id, DistrictDto dto, boolean update) {
        StatusMessage statusMessage;
        String districtCodeDto = dto.getCode();
        String districtNameDto = dto.getName();
        UUID districtIdDto = null;
        if (update) {
            districtIdDto = ObjectUtils.isEmpty(id) ? dto.getId() : id;
        }

        if (ObjectUtils.isEmpty(dto)) {
            return StatusMessage.DISTRICT_IS_NULL;
        }
        if (update && (ObjectUtils.isEmpty(districtIdDto))) {
            return StatusMessage.DISTRICT_ID_IS_NULL;
        }
        if (update && (ObjectUtils.isEmpty(districtIdDto))) {
            return StatusMessage.DISTRICT_ID_NOT_EXISTS;
        }
        if (!StringUtils.hasText(dto.getName())) {
            return StatusMessage.NAME_IS_NULL;
        }
        if (!StringUtils.hasText(dto.getCode())) {
            return StatusMessage.DISTRICT_CODE_IS_NULL;
        }

        if ((!update && existsByDistrictCode(districtCodeDto) || (update && !dto.getCode().equals(districtCodeDto) && existsByDistrictCode(districtCodeDto)))) {
            return StatusMessage.DISTRICT_CODE_IS_EXIST;
        }
        if ((!update && existsByDistrictName(districtNameDto) || (update && !dto.getName().equals(districtNameDto) && existsByDistrictName(districtNameDto)))) {
            return StatusMessage.DISTRICT_NAME_IS_EXIST;
        }

        if (!ObjectUtils.isEmpty(dto.getCommuneDtoList())) {
            for (CommuneDto communeDto : dto.getCommuneDtoList()) {
                statusMessage = communeHandle.validateCommune(communeDto.getId(), communeDto, update);
                if (StatusMessage.SUCCESS != statusMessage) {
                    return statusMessage;
                }
            }
        }


        return StatusMessage.SUCCESS;

    }

    public StatusMessage validateSetProvince(DistrictDto dto) {
        ProvinceDto provinceDto = dto.getProvinceDto();
        if (!ObjectUtils.isEmpty(provinceDto)) {
            if ((ObjectUtils.isEmpty(provinceDto.getId()))) {
                return StatusMessage.ID_PROVINCE_IS_NULL;
            }
            if (!provinceRepository.existsById(provinceDto.getId())) {
                return StatusMessage.PROVINCE_ID_NOT_EXISTS;
            }
        }
        return StatusMessage.SUCCESS;
    }
}