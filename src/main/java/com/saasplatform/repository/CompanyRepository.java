package com.saasplatform.repository;

import com.saasplatform.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByTenantId(String tenantId);
    
    List<Company> findByStatus(Company.CompanyStatus status);
    
    long countByStatus(Company.CompanyStatus status);
}

