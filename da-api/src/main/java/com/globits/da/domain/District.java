package com.globits.da.domain;

import com.globits.core.domain.BaseObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "tbl_district")
@XmlRootElement
@NoArgsConstructor
public class District extends BaseObject {
    private static final long serialVersionUID = 1L;


    @Column(name = "code", length = 10, unique = true)
    private String code;
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    @OneToMany(mappedBy = "district", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Commune> communeList;


}
