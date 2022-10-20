package com.globits.da.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.globits.core.domain.BaseObject;
import com.globits.da.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tbl_certificate")
@XmlRootElement
public class Certificate extends BaseObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "code", length = 10, unique = true)
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "effective_date")
    private LocalDate effectiveDate;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;


}
