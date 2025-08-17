package com.saasplatform.service;

import com.saasplatform.entity.Notification;
import com.saasplatform.entity.User;
import com.saasplatform.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    /**
     * Create a notification for quote approval
     */
    public Notification createQuoteApprovalNotification(Long quoteId, String quoteTitle, User sender, String tenantId) {
        // Find company admin users for this tenant
        List<User> companyAdmins = userService.getUsersByRole(tenantId, "COMPANY_ADMIN");
        
        if (companyAdmins.isEmpty()) {
            log.warn("No company admin found for tenant: {}", tenantId);
            return null;
        }

        // Create notification for the first company admin (or you could create for all)
        User recipient = companyAdmins.get(0);
        
        Notification notification = Notification.createApprovalNotification(
            "QUOTE", quoteId, quoteTitle, recipient, sender
        );
        
        notification.setTenantId(tenantId);
        notification.setCreatedBy(sender.getEmail());
        notification.setUpdatedBy(sender.getEmail());
        
        return notificationRepository.save(notification);
    }

    /**
     * Create a notification for booking approval
     */
    public Notification createBookingApprovalNotification(Long bookingId, String bookingTitle, User sender, String tenantId) {
        // Find company admin users for this tenant
        List<User> companyAdmins = userService.getUsersByRole(tenantId, "COMPANY_ADMIN");
        
        if (companyAdmins.isEmpty()) {
            log.warn("No company admin found for tenant: {}", tenantId);
            return null;
        }

        // Create notification for the first company admin
        User recipient = companyAdmins.get(0);
        
        Notification notification = Notification.createApprovalNotification(
            "BOOKING", bookingId, bookingTitle, recipient, sender
        );
        
        notification.setTenantId(tenantId);
        notification.setCreatedBy(sender.getEmail());
        notification.setUpdatedBy(sender.getEmail());
        
        return notificationRepository.save(notification);
    }

    /**
     * Get all notifications for a user
     */
    public List<Notification> getUserNotifications(String tenantId, Long userId) {
        return notificationRepository.findByTenantIdAndRecipientId(tenantId, userId);
    }

    /**
     * Get unread notifications for a user
     */
    public List<Notification> getUnreadNotifications(String tenantId, Long userId) {
        return notificationRepository.findByTenantIdAndRecipientIdAndStatus(tenantId, userId, Notification.NotificationStatus.UNREAD);
    }

    /**
     * Mark notification as read
     */
    public Notification markAsRead(String tenantId, Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findByTenantIdAndId(tenantId, notificationId);
        
        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.markAsRead();
            notification.setUpdatedBy("system");
            return notificationRepository.save(notification);
        }
        
        return null;
    }

    /**
     * Mark all notifications as read for a user
     */
    public void markAllAsRead(String tenantId, Long userId) {
        List<Notification> unreadNotifications = getUnreadNotifications(tenantId, userId);
        unreadNotifications.forEach(notification -> {
            notification.markAsRead();
            notification.setUpdatedBy("system");
        });
        notificationRepository.saveAll(unreadNotifications);
    }

    /**
     * Delete notification
     */
    public void deleteNotification(String tenantId, Long notificationId) {
        Optional<Notification> notificationOpt = notificationRepository.findByTenantIdAndId(tenantId, notificationId);
        notificationOpt.ifPresent(notificationRepository::delete);
    }

    /**
     * Get notification count for a user
     */
    public long getUnreadNotificationCount(String tenantId, Long userId) {
        return notificationRepository.countByTenantIdAndRecipientIdAndStatus(tenantId, userId, Notification.NotificationStatus.UNREAD);
    }
}





