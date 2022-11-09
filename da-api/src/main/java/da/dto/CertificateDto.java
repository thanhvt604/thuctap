package com.globits.da.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.globits.core.dto.BaseObjectDto;
import com.globits.da.Constants;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CertificateDto extends BaseObjectDto {

    private String name;
    private String code;

    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDate effectiveDate;
    @JsonFormat(pattern = Constants.DATE_TIME_FORMAT)
    private LocalDate expirationDate;


}
