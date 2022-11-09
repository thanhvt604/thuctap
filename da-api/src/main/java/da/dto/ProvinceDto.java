package com.globits.da.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.globits.core.dto.BaseObjectDto;
import com.globits.da.domain.Province;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDto extends BaseObjectDto {
    private String name;
    private String code;
    private List<DistrictDto> districtDtoList;



    public static ProvinceDto toDto(Province entity) {
        ProvinceDto provinceDto = new ModelMapper().map(entity, ProvinceDto.class);
        provinceDto.setDistrictDtoList(entity.getDistrictList().stream().map(district ->new ModelMapper().map(district, DistrictDto.class)).collect(Collectors.toList()));
        return provinceDto;

    }


}
