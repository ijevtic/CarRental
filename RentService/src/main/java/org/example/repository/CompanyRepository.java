package org.example.repository;

import org.example.domain.CarType;
import org.example.domain.Company;
import org.example.mapper.CompanyMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findCompanyByCompanyName(String name);
    Optional<Company> findCompanyById(Long id);
    Optional<Company> getReferenceById(Long id);

    @Modifying
    @Query("update Company c set c.companyName = :companyName, c.description = :description where c.id = :id")
    void updateCompanyById(Long id, String companyName, String description);
}