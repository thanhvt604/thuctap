package com.globits.da.repository;

import com.globits.da.domain.EmployeeCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface EmployeeCertificateRepository extends JpaRepository<EmployeeCertificate, UUID> {

    EmployeeCertificate getById(UUID id);

    @Query(value = "SELECT COUNT(*) FROM tbl_employee_certificate WHERE employee_id=:employeeId " +
            "and certificate_id=:certificateId "+
            "and province_id=:provinceId " +
            "and expiration_date>:nowDate", nativeQuery = true)
    int hasCertificateOfProvince(@Param("employeeId") String employeeId,@Param("certificateId") String certificateId, @Param("provinceId") String provinceId, @Param("nowDate") LocalDate nowDate);

    @Query(value = "SELECT COUNT(*) FROM tbl_employee_certificate WHERE certificate_id=:certificateId " +
            "and expiration_date>:nowDate", nativeQuery = true)
    int hasThreeCertificateSameType(@Param("certificateId") String certificateId, @Param("nowDate") LocalDate nowDate);

}
