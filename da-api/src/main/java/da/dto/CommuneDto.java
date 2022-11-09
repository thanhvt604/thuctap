package com.globits.da.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.globits.core.dto.BaseObjectDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommuneDto extends BaseObjectDto {
    private String name;
    private String code;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private DistrictDto districtDto;


}
