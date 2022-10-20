package com.globits.da.support.ErrorHandle;

import com.globits.da.dto.CertificateDto;
import com.globits.da.repository.CertificateRepository;
import com.globits.da.support.StatusMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import com.globits.da.Constants;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class CertificateHandle {
    @Autowired
    private CertificateRepository certificateRepository;

    public Boolean existsByCertificateCode(String code) {
        return certificateRepository.countCode(code) != 0;
    }

    public StatusMessage certificateValidate(UUID id, CertificateDto dto) {
        if (ObjectUtils.isEmpty(dto)) {
            return StatusMessage.CERTIFICATE_IS_NULL;
        }
        if (!StringUtils.hasText(dto.getCode())) {
            return StatusMessage.CERTIFICATE_CODE_IS_NULL;
        }
        if ((id == null && existsByCertificateCode(dto.getCode())) || (id != null && !dto.getCode().equals(certificateRepository.getById(id).getCode()) && existsByCertificateCode(dto.getCode()))) {
            return StatusMessage.COMMUNE_CODE_IS_EXIST;
        }
        if (!StringUtils.hasText(dto.getName())) {
            return StatusMessage.NAME_IS_NULL;
        }
        if (ObjectUtils.isEmpty(dto.getEffectiveDate())) {
            return StatusMessage.EFFECTIVE_DATE_IS_NULL;
        }
        if (ObjectUtils.isEmpty(dto.getExpirationDate())) {
            return StatusMessage.EXPIRATION_DATE_IS_NULL;
        }
        Pattern DATE_PATTERN = Pattern.compile(Constants.REGEX_DATE);
        if(!DATE_PATTERN.matcher(dto.getExpirationDate().toString()).matches()||!DATE_PATTERN.matcher(dto.getEffectiveDate().toString()).matches())
        {
            return StatusMessage.WRONG_DATE_FORMAT;
        }
        if (dto.getEffectiveDate().isAfter(dto.getExpirationDate())) {
            return StatusMessage.WRONG_POSITION_FOR_DATE;
        }
        return StatusMessage.SUCCESS;
    }


}
