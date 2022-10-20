package com.globits.da.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.globits.core.dto.BaseObjectDto;
import com.globits.da.domain.District;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

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

    public DistrictDto toDto(District entity)
    {
        DistrictDto districtDto;
        districtDto=new ModelMapper().map(entity,DistrictDto.class);
        districtDto.setCommuneDtoList(entity.getCommuneList().stream().map(commune -> new ModelMapper().map(commune,CommuneDto.class)).collect(Collectors.toList()));
        return districtDto;
    }

}