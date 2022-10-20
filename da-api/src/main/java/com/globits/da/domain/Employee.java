package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tbl_employee")
@XmlRootElement
public class Employee extends BaseObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "code", unique = true)
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "age")
    private int age;
    @Column(name = "province_id", columnDefinition = "CHAR(64)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID provinceId;
    @Column(name = "district_id", columnDefinition = "CHAR(64)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID districtId;
    @Column(name = "commune_id", columnDefinition = "CHAR(64)")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID communeId;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    List<EmployeeCertificate> employeeCertificates;

}
