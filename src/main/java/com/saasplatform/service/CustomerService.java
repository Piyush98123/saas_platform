package com.saasplatform.service;

import com.saasplatform.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<Customer> getAllCustomers(String tenantId);
    
    Optional<Customer> getCustomerById(String tenantId, Long id);
    
    Optional<Customer> getCustomerByNumber(String tenantId, String customerNumber);
    
    Customer createCustomer(String tenantId, Customer customer);
    
    Customer updateCustomer(String tenantId, Long id, Customer customer);
    
    void deleteCustomer(String tenantId, Long id);
    
    List<Customer> searchCustomers(String tenantId, String searchTerm);
    
    List<Customer> getCustomersByStatus(String tenantId, Customer.CustomerStatus status);
    
    List<Customer> getCustomersByType(String tenantId, Customer.CustomerType customerType);
    
    boolean existsByCustomerNumber(String tenantId, String customerNumber);
    
    boolean existsByEmail(String tenantId, String email);
    
    String generateCustomerNumber(String tenantId);
}





