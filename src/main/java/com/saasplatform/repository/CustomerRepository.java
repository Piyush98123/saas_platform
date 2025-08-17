package com.saasplatform.repository;

import com.saasplatform.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByTenantId(String tenantId);
    
    Optional<Customer> findByTenantIdAndCustomerNumber(String tenantId, String customerNumber);
    
    Optional<Customer> findByTenantIdAndEmail(String tenantId, String email);
    
    List<Customer> findByTenantIdAndStatus(String tenantId, Customer.CustomerStatus status);
    
    List<Customer> findByTenantIdAndCustomerType(String tenantId, Customer.CustomerType customerType);
    
    @Query("SELECT c FROM Customer c WHERE c.tenantId = :tenantId AND " +
           "(LOWER(c.companyName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.contactPerson) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<Customer> findByTenantIdAndSearchTerm(@Param("tenantId") String tenantId, @Param("searchTerm") String searchTerm);
    
    boolean existsByTenantIdAndCustomerNumber(String tenantId, String customerNumber);
    
    boolean existsByTenantIdAndEmail(String tenantId, String email);
    
    long countByTenantId(String tenantId);
}
