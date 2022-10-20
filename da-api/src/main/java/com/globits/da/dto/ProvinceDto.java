package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import com.globits.da.domain.Province;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProvinceDto extends BaseObjectDto {
    private String name;
    private String code;

    List<DistrictDto> districtDtoList;

    public ProvinceDto toDto(Province entity) {
        ProvinceDto provinceDto ;
        provinceDto = new ModelMapper().map(entity, ProvinceDto.class);
        provinceDto.setDistrictDtoList(entity.getDistrictList().stream().map(district ->new ModelMapper().map(district,DistrictDto.class)).collect(Collectors.toList()));
        return provinceDto;

    }


}
