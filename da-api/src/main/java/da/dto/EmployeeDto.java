package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto extends BaseObjectDto {
    private String code;
    private String name;
    private String phoneNumber;
    private String email;
    private Integer age;
    private ProvinceDto provinceDto;
    private DistrictDto districtDto;
    private CommuneDto communeDto;


}
