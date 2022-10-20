package com.globits.da.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.globits.da.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCertificateDto {

    @JsonFormat(pattern = Constants.dateTimeFormat)
    private LocalDate effectiveDate;
    @JsonFormat(pattern = Constants.dateTimeFormat)
    private  LocalDate expirationDate;

    EmployeeDto employeeDto;
    ProvinceDto provinceDto;
    CertificateDto certificateDto;

}
