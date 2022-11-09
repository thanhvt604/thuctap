package com.globits.da.support.handle;

import com.globits.da.Constants;
import com.globits.da.domain.Commune;
import com.globits.da.domain.District;
import com.globits.da.dto.CommuneDto;
import com.globits.da.dto.DistrictDto;
import com.globits.da.repository.CommuneRepository;
import com.globits.da.repository.DistrictRepository;
import com.globits.da.support.StatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Component
public class CommuneHandle {

    @Autowired
    private CommuneRepository communeRepository;
    @Autowired
    private DistrictRepository districtRepository;


    public Boolean existsByCommuneCode(String code) {
        return communeRepository.countCode(code) != Constants.NOT_HAS;
    }

    public Boolean existsByCommuneName(String name) {
        return communeRepository.countName(name) != Constants.NOT_HAS;
    }

    public String getCode(UUID id) {
        return communeRepository.getById(id).getCode();
    }

    public String getName(UUID id) {
        return communeRepository.getById(id).getName();
    }

    public Commune toCommuneEntity(UUID id, CommuneDto dto, District district, boolean update) {
        Commune commune = !update ? new Commune() : communeRepository.getById(id);
        commune.setCode(dto.getCode());
        commune.setName(dto.getName());

        if (!ObjectUtils.isEmpty(dto.getDistrictDto())) {
            commune.setDistrict(districtRepository.getById(dto.getDistrictDto().getId()));
        }

        if (!ObjectUtils.isEmpty(district)) {
            commune.setDistrict(district);
        }
        return commune;
    }

    public List<Commune> toEntityList(List<CommuneDto> dtoList, District district, boolean update) {

        return dtoList.stream().map(communeDto -> toCommuneEntity(communeDto.getId(), communeDto, district, update)).collect(Collectors.toList());

    }

    public StatusMessage validateCommune(UUID id, CommuneDto dto, boolean update) {
        String communeCodeDto = dto.getCode();
        String communeNameDto = dto.getName();
        UUID communeIdDto = null;
        if (update) {
            communeIdDto = ObjectUtils.isEmpty(id) ? dto.getId() : id;
        }

        if (ObjectUtils.isEmpty(dto)) {
            return StatusMessage.COMMUNE_IS_NULL;
        }
        if (update && ObjectUtils.isEmpty(communeIdDto)) {
            return StatusMessage.COMMUNE_ID_IS_NULL;
        }
        if (update && (!communeRepository.existsById(communeIdDto))) {
            return StatusMessage.COMMUNE_ID_IS_NOT_EXIST;
        }
        if (!StringUtils.hasText(dto.getName())) {
            return StatusMessage.COMMUNE_NAME_IS_NULL;
        }
        if (!StringUtils.hasText(dto.getCode())) {
            return StatusMessage.COMMUNE_CODE_IS_NULL;
        }


        if ((!update && existsByCommuneCode(communeCodeDto)) || (update && !communeCodeDto.equals(getCode(id)) && existsByCommuneCode(communeCodeDto))) {
            return StatusMessage.COMMUNE_CODE_IS_EXIST;
        }
        if ((!update && existsByCommuneName(communeNameDto)) || (update && !communeNameDto.equals(getName(id)) && existsByCommuneName(communeNameDto))) {
            return StatusMessage.COMMUNE_NAME_IS_EXISTS;
        }

        return StatusMessage.SUCCESS;
    }


    public StatusMessage setDistrictValidate(CommuneDto dto) {
        DistrictDto districtDto = dto.getDistrictDto();
        if (!ObjectUtils.isEmpty(dto.getDistrictDto())) {
            if (ObjectUtils.isEmpty(districtDto.getId())) {
                return StatusMessage.DISTRICT_ID_IS_NULL;
            }

            if (!districtRepository.existsById(districtDto.getId())) {
                return StatusMessage.DISTRICT_ID_NOT_EXISTS;
            }
        }
        return StatusMessage.SUCCESS;
    }

}
