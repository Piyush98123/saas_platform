package com.saasplatform.service;

import com.saasplatform.entity.Company;
import java.util.List;
import java.util.Optional;

public interface CompanyService {
    
    List<Company> getAllCompanies();
    
    Optional<Company> getCompanyById(Long id);
    
    Company createCompany(Company company);
    
    Company updateCompany(Long id, Company companyDetails);
    
    void deleteCompany(Long id);
    
    long getCompanyCount();
    
    long getActiveCompanyCount();
    
    List<Company> getCompaniesByStatus(Company.CompanyStatus status);
    
    Optional<Company> getCompanyByTenantId(String tenantId);
}



