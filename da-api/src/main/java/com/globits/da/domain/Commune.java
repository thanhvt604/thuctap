package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@Entity
@Table(name = "tbl_commune")
@XmlRootElement
public class Commune extends BaseObject {

    private static final long serialVersionUID = 1L;

    @Column(name = "code", length = 10, unique = true)
    private String code;
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "district_id", nullable = false)
    District district;

}

