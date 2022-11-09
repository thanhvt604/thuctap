package com.globits.da.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.globits.core.dto.BaseObjectDto;
import com.globits.da.domain.District;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class DistrictDto extends BaseObjectDto {
    private String name;
    private String code;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ProvinceDto provinceDto;
    private List<CommuneDto> communeDtoList;
    @Autowired
    private static ModelMapper modelMapper;

    public static DistrictDto toDto(District entity) {
        DistrictDto districtDto;
        districtDto = modelMapper.map(entity, DistrictDto.class);
        if (!ObjectUtils.isEmpty(entity.getCommuneList())) {
            districtDto.setCommuneDtoList(entity.getCommuneList().stream().map(commune -> modelMapper.map(commune, CommuneDto.class)).collect(Collectors.toList()));
        }
        return districtDto;
    }

}