package com.globits.da.repository;

import com.globits.da.domain.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, UUID> {

    Province getById(UUID id);

    @Query(value = "select count(*) from tbl_province  where code=:code", nativeQuery = true)
    int countCode(@Param("code") String code);


}

