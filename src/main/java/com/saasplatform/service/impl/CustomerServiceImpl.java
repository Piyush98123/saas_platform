package com.saasplatform.service.impl;

import com.saasplatform.entity.Customer;
import com.saasplatform.repository.CustomerRepository;
import com.saasplatform.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> getAllCustomers(String tenantId) {
        return customerRepository.findByTenantId(tenantId);
    }

    @Override
    public Optional<Customer> getCustomerById(String tenantId, Long id) {
        return customerRepository.findById(id)
                .filter(customer -> customer.getTenantId().equals(tenantId));
    }

    @Override
    public Optional<Customer> getCustomerByNumber(String tenantId, String customerNumber) {
        return customerRepository.findByTenantIdAndCustomerNumber(tenantId, customerNumber);
    }

    @Override
    public Customer createCustomer(String tenantId, Customer customer) {
        customer.setTenantId(tenantId);
        customer.setCustomerNumber(generateCustomerNumber(tenantId));
        customer.setCreatedBy("system");
        customer.setUpdatedBy("system");
        
        if (customer.getStatus() == null) {
            customer.setStatus(Customer.CustomerStatus.PROSPECT);
        }
        
        if (customer.getCustomerType() == null) {
            customer.setCustomerType(Customer.CustomerType.SMALL_BUSINESS);
        }
        
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(String tenantId, Long id, Customer customerDetails) {
        Customer customer = getCustomerById(tenantId, id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        customer.setCompanyName(customerDetails.getCompanyName());
        customer.setContactPerson(customerDetails.getContactPerson());
        customer.setEmail(customerDetails.getEmail());
        customer.setPhone(customerDetails.getPhone());
        customer.setWebsite(customerDetails.getWebsite());
        customer.setAddress(customerDetails.getAddress());
        customer.setCity(customerDetails.getCity());
        customer.setState(customerDetails.getState());
        customer.setCountry(customerDetails.getCountry());
        customer.setZipCode(customerDetails.getZipCode());
        customer.setStatus(customerDetails.getStatus());
        customer.setCustomerType(customerDetails.getCustomerType());
        customer.setIndustry(customerDetails.getIndustry());
        customer.setAnnualRevenue(customerDetails.getAnnualRevenue());
        customer.setEmployeeCount(customerDetails.getEmployeeCount());
        customer.setSource(customerDetails.getSource());
        customer.setNotes(customerDetails.getNotes());
        customer.setLastContactDate(customerDetails.getLastContactDate());
        customer.setNextFollowUpDate(customerDetails.getNextFollowUpDate());
        customer.setCreditLimit(customerDetails.getCreditLimit());
        customer.setPaymentTerms(customerDetails.getPaymentTerms());
        customer.setUpdatedBy("system");
        
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(String tenantId, Long id) {
        Customer customer = getCustomerById(tenantId, id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepository.delete(customer);
    }

    @Override
    public List<Customer> searchCustomers(String tenantId, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllCustomers(tenantId);
        }
        return customerRepository.findByTenantIdAndSearchTerm(tenantId, searchTerm.trim());
    }

    @Override
    public List<Customer> getCustomersByStatus(String tenantId, Customer.CustomerStatus status) {
        return customerRepository.findByTenantIdAndStatus(tenantId, status);
    }

    @Override
    public List<Customer> getCustomersByType(String tenantId, Customer.CustomerType customerType) {
        return customerRepository.findByTenantIdAndCustomerType(tenantId, customerType);
    }

    @Override
    public boolean existsByCustomerNumber(String tenantId, String customerNumber) {
        return customerRepository.existsByTenantIdAndCustomerNumber(tenantId, customerNumber);
    }

    @Override
    public boolean existsByEmail(String tenantId, String email) {
        return customerRepository.existsByTenantIdAndEmail(tenantId, email);
    }

    @Override
    public String generateCustomerNumber(String tenantId) {
        // Simple customer number generation: CUST-YYYY-XXXX
        String year = String.valueOf(LocalDate.now().getYear());
        long count = customerRepository.countByTenantId(tenantId) + 1;
        return String.format("CUST-%s-%04d", year, count);
    }
}





