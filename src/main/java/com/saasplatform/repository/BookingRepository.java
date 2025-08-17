package com.saasplatform.repository;

import com.saasplatform.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByTenantId(String tenantId);
    
    Optional<Booking> findByTenantIdAndId(String tenantId, Long id);
    
    List<Booking> findByTenantIdAndStatus(String tenantId, Booking.BookingStatus status);
    
    List<Booking> findByTenantIdAndCustomerId(String tenantId, Long customerId);
    
    List<Booking> findByTenantIdAndScheduledDateBetween(String tenantId, LocalDateTime startDate, LocalDateTime endDate);
    
    List<Booking> findByTenantIdAndApprovalRequired(String tenantId, Boolean approvalRequired);
    
    List<Booking> findByTenantIdAndStatusIn(String tenantId, List<Booking.BookingStatus> statuses);
}





