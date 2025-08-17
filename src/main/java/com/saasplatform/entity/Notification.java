package com.saasplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.UNREAD;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "action_required")
    private Boolean actionRequired = false;

    @Column(name = "action_url")
    private String actionUrl;

    @Column(name = "related_entity_type")
    private String relatedEntityType; // QUOTE, BOOKING, LEAD, etc.

    @Column(name = "related_entity_id")
    private Long relatedEntityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id", nullable = false)
    private User recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(name = "priority")
    @Enumerated(EnumType.STRING)
    private NotificationPriority priority = NotificationPriority.NORMAL;

    public enum NotificationType {
        QUOTE_APPROVAL, BOOKING_APPROVAL, LEAD_ASSIGNMENT, 
        PAYMENT_RECEIVED, SYSTEM_ALERT, USER_INVITATION
    }

    public enum NotificationStatus {
        UNREAD, READ, ARCHIVED
    }

    public enum NotificationPriority {
        LOW, NORMAL, HIGH, URGENT
    }

    // Helper method to mark as read
    public void markAsRead() {
        this.status = NotificationStatus.READ;
        this.readAt = LocalDateTime.now();
    }

    // Helper method to create approval notification
    public static Notification createApprovalNotification(String entityType, Long entityId, String entityName, User recipient, User sender) {
        Notification notification = new Notification();
        notification.setTitle("Approval Required");
        notification.setMessage(String.format("A new %s '%s' requires your approval", entityType.toLowerCase(), entityName));
        notification.setType(entityType.equals("QUOTE") ? NotificationType.QUOTE_APPROVAL : NotificationType.BOOKING_APPROVAL);
        notification.setRecipient(recipient);
        notification.setSender(sender);
        notification.setActionRequired(true);
        notification.setActionUrl(String.format("/%s/%d", entityType.toLowerCase(), entityId));
        notification.setRelatedEntityType(entityType);
        notification.setRelatedEntityId(entityId);
        notification.setPriority(NotificationPriority.HIGH);
        return notification;
    }
}



