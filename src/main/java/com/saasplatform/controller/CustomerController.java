package com.saasplatform.controller;

import com.saasplatform.entity.Customer;
import com.saasplatform.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomers(
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type) {
        
        try {
            List<Customer> customers;
            
            if (search != null && !search.trim().isEmpty()) {
                customers = customerService.searchCustomers(tenantId, search);
            } else if (status != null && !status.trim().isEmpty()) {
                customers = customerService.getCustomersByStatus(tenantId, Customer.CustomerStatus.valueOf(status.toUpperCase()));
            } else if (type != null && !type.trim().isEmpty()) {
                customers = customerService.getCustomersByType(tenantId, Customer.CustomerType.valueOf(type.toUpperCase()));
            } else {
                customers = customerService.getAllCustomers(tenantId);
            }
            
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            log.error("Error fetching customers", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable Long id) {
        
        try {
            return customerService.getCustomerById(tenantId, id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching customer with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<Customer> createCustomer(
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestBody Customer customer) {
        
        try {
            Customer createdCustomer = customerService.createCustomer(tenantId, customer);
            return ResponseEntity.ok(createdCustomer);
        } catch (Exception e) {
            log.error("Error creating customer", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable Long id,
            @RequestBody Customer customer) {
        
        try {
            Customer updatedCustomer = customerService.updateCustomer(tenantId, id, customer);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            log.error("Error updating customer with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable Long id) {
        
        try {
            customerService.deleteCustomer(tenantId, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting customer with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/check/number/{customerNumber}")
    public ResponseEntity<Boolean> checkCustomerNumberExists(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable String customerNumber) {
        
        try {
            boolean exists = customerService.existsByCustomerNumber(tenantId, customerNumber);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            log.error("Error checking customer number: {}", customerNumber, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/check/email/{email}")
    public ResponseEntity<Boolean> checkEmailExists(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable String email) {
        
        try {
            boolean exists = customerService.existsByEmail(tenantId, email);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            log.error("Error checking email: {}", email, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}





