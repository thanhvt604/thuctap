package com.globits.da.repository;

import com.globits.da.domain.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DistrictRepository extends JpaRepository<District, UUID> {

    District getById(UUID id);

    @Query(value = "select count(*) from tbl_district  where code=:code", nativeQuery = true)
    int countCode(@Param("code") String code);
}
