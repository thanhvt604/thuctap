package com.globits.da.support.ErrorHandle;

import com.globits.da.domain.District;
import com.globits.da.domain.Province;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.dto.ProvinceDto;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.support.StatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class DistrictHandle {

    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private CommuneHandle communeHandle;

    public Boolean existsByDistrictCode(String code) {
        return districtRepository.countCode(code) != 0;
    }

    public District toDistrictEntity(DistrictDto districtDto, Province province) {
        District district = new District();
        district.setCode(districtDto.getCode());
        district.setName(districtDto.getName());
        district.setProvince(province);
        if (!ObjectUtils.isEmpty(districtDto.getCommuneDtoList())) {
            district.setCommuneList(districtDto.getCommuneDtoList().stream().map(communeDto -> communeHandle.toEntity(communeDto, district)).collect(Collectors.toList()));
        }
        return district;
    }

    public List<District> toEntityList(List<DistrictDto> dtoList, Province province) {
        List<District> districts = dtoList.stream().map(districtDto -> toDistrictEntity(districtDto, province)).collect(Collectors.toList());
        return districts;
    }

    public List<District> updateDistrictList(ProvinceDto dto) {
        List<District> districtList = new ArrayList<>();
        District district;
        for (DistrictDto districtDto : dto.getDistrictDtoList()) {
            if (districtDto.getId() != null) {
                {
                    district = districtRepository.getById(districtDto.getId());
                    district.setCode(districtDto.getCode());
                    district.setName(districtDto.getName());
                    districtList.add(district);
                }
            }
        }
        return districtList;

    }

    public StatusMessage validateDistrict(UUID id, DistrictDto dto) {
        StatusMessage statusMessage;
        if (id != null && !districtRepository.existsById(id)) {
            return StatusMessage.DISTRICT_ID_NOT_EXISTS;
        }
        if (ObjectUtils.isEmpty(dto)) {
            return StatusMessage.DISTRICT_IS_NULL;
        }
        if (StringUtils.isEmpty(dto.getName())) {
            return StatusMessage.NAME_IS_NULL;
        }
        if (StringUtils.isEmpty(dto.getCode())) {
            return StatusMessage.DISTRICT_CODE_IS_NULL;
        }

        if ((id == null && existsByDistrictCode(dto.getCode())) || (id != null && !dto.getCode().equals(districtRepository.getById(id).getCode()) && existsByDistrictCode(dto.getCode()))) {
            return StatusMessage.DISTRICT_CODE_IS_EXIST;
        }


        if (dto.getCommuneDtoList() != null) {
            for (CommuneDto communeDto : dto.getCommuneDtoList()) {
                statusMessage = communeHandle.validateCommune(communeDto.getId(), communeDto);
                if (StatusMessage.SUCCESS != statusMessage) {
                    return statusMessage;
                }
            }
        }
        return StatusMessage.SUCCESS;


    }
}