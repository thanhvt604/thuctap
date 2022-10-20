package com.globits.da.repository;

import com.globits.da.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {

    Employee getById(UUID id);

    List<Employee> findAll();

    @Query(value = "select count(*) from tbl_employee  where code=:code", nativeQuery = true)
    int countCode(@Param("code") String code);
}
