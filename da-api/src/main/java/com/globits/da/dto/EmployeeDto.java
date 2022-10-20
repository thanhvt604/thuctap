package com.globits.da.dto;

import com.globits.core.dto.BaseObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto extends BaseObjectDto {
    private String code;
    private String name;
    private String phoneNumber;
    private String email;
    private int age;
    private UUID provinceId;
    private UUID districtId;
    private UUID communeId;


}
