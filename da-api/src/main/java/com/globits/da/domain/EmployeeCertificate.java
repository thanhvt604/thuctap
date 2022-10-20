package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tbl_employee_certificate")
@XmlRootElement
public class EmployeeCertificate extends BaseObject {

    @Column(name="effective_date")
    private LocalDate effectiveDate;
    @Column(name="expiration_date")
    private  LocalDate expirationDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "province_id")
    Province province;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="certificate_id")
    Certificate certificate;
}
