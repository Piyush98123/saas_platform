package com.saasplatform.service.impl;

import com.saasplatform.entity.Company;
import com.saasplatform.repository.CompanyRepository;
import com.saasplatform.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> getCompanyById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public Company createCompany(Company company) {
        company.setCreatedAt(LocalDateTime.now());
        company.setUpdatedAt(LocalDateTime.now());
        company.setCreatedBy("system");
        company.setUpdatedBy("system");
        
        if (company.getStatus() == null) {
            company.setStatus(Company.CompanyStatus.ACTIVE);
        }
        
        return companyRepository.save(company);
    }

    @Override
    public Company updateCompany(Long id, Company companyDetails) {
        Optional<Company> companyOpt = companyRepository.findById(id);
        if (companyOpt.isPresent()) {
            Company company = companyOpt.get();
            
            company.setName(companyDetails.getName());
            company.setEmail(companyDetails.getEmail());
            company.setPhone(companyDetails.getPhone());
            company.setAddress(companyDetails.getAddress());
            company.setCity(companyDetails.getCity());
            company.setState(companyDetails.getState());
            company.setCountry(companyDetails.getCountry());
            company.setZipCode(companyDetails.getZipCode());
            company.setWebsite(companyDetails.getWebsite());
            company.setStatus(companyDetails.getStatus());
            company.setPlan(companyDetails.getPlan());
            company.setUpdatedAt(LocalDateTime.now());
            company.setUpdatedBy("system");
            
            return companyRepository.save(company);
        }
        return null;
    }

    @Override
    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    @Override
    public long getCompanyCount() {
        return companyRepository.count();
    }

    @Override
    public long getActiveCompanyCount() {
        return companyRepository.countByStatus(Company.CompanyStatus.ACTIVE);
    }

    @Override
    public List<Company> getCompaniesByStatus(Company.CompanyStatus status) {
        return companyRepository.findByStatus(status);
    }

    @Override
    public Optional<Company> getCompanyByTenantId(String tenantId) {
        Company company = companyRepository.findByTenantId(tenantId);
        return Optional.ofNullable(company);
    }
}
