package com.saasplatform.repository;

import com.saasplatform.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Long> {

    List<Quote> findByTenantId(String tenantId);
    
    Optional<Quote> findByTenantIdAndId(String tenantId, Long id);
    
    List<Quote> findByTenantIdAndStatus(String tenantId, Quote.QuoteStatus status);
    
    List<Quote> findByTenantIdAndCustomerId(String tenantId, Long customerId);
    
    List<Quote> findByTenantIdAndApprovalRequired(String tenantId, Boolean approvalRequired);
    
    List<Quote> findByTenantIdAndStatusIn(String tenantId, List<Quote.QuoteStatus> statuses);
}





