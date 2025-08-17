package com.saasplatform.repository;

import com.saasplatform.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByTenantId(String tenantId);
    
    Optional<Notification> findByTenantIdAndId(String tenantId, Long id);
    
    List<Notification> findByTenantIdAndRecipientId(String tenantId, Long recipientId);
    
    List<Notification> findByTenantIdAndRecipientIdAndStatus(String tenantId, Long recipientId, Notification.NotificationStatus status);
    
    long countByTenantIdAndRecipientIdAndStatus(String tenantId, Long recipientId, Notification.NotificationStatus status);
    
    List<Notification> findByTenantIdAndRecipientIdOrderByCreatedAtDesc(String tenantId, Long recipientId);
}



