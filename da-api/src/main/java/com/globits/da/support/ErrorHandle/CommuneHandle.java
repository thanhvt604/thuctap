package com.globits.da.support.ErrorHandle;

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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Component
public class CommuneHandle {
    @Autowired
    private CommuneRepository communeRepository;
    @Autowired
    private DistrictRepository districtRepository;

    public Commune toEntity(CommuneDto dto, District district) {
        Commune commune = new Commune();
        commune.setCode(dto.getCode());
        commune.setName(dto.getName());
        commune.setDistrict(district);
        return commune;
    }

    public Boolean existsByCommuneCode(String code) {
        return communeRepository.countCode(code) != 0;
    }

    public List<Commune> updateCommuneList(DistrictDto dto, UUID id) {
        List<Commune> communeList = new ArrayList<>();
        Commune commune;
        for (CommuneDto communeDto : dto.getCommuneDtoList()) {

            commune = communeRepository.getById(communeDto.getId());
            commune.setCode(communeDto.getCode());
            commune.setName(communeDto.getName());
            commune.setDistrict(districtRepository.getById(id));
            communeList.add(commune);

        }
        return communeList;

    }

    public StatusMessage validateCommune(UUID id, CommuneDto dto) {
        if (ObjectUtils.isEmpty(dto)) {
            return StatusMessage.COMMUNE_IS_NULL;
        }
        if (StringUtils.isEmpty(dto.getName())) {
            return StatusMessage.COMMUNE_NAME_IS_NULL;
        }
        if (StringUtils.isEmpty(dto.getCode())) {
            return StatusMessage.COMMUNE_CODE_IS_NULL;
        }
        if (id != null && !communeRepository.existsById(id)) {
            return StatusMessage.COMMUNE_ID_IS_NOT_EXIST;
        }
        if ((id==null&&existsByCommuneCode(dto.getCode()))||(id!=null&&!dto.getCode().equals(communeRepository.getById(id).getCode())&&existsByCommuneCode(dto.getCode()))) {
            return StatusMessage.COMMUNE_CODE_IS_EXIST;
        }

        return StatusMessage.SUCCESS;
    }

}
