package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_province")
@XmlRootElement
public class Province extends BaseObject {
    private static final long serialVersionUID = 1L;

    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "province", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<District> districtList = new ArrayList<>();

}
