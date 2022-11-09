package com.globits.da.repository;

import com.globits.da.domain.Commune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, UUID> {

    Commune getById(UUID id);

    @Query(value = "select count(*) from tbl_commune  where code=:code", nativeQuery = true)
    int countCode(@Param("code") String code);

    @Query(value = "select count(*) from tbl_commune  where name=:name", nativeQuery = true)
    int countName(@Param("name") String name);
}
