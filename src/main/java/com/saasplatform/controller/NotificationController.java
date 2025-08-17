package com.saasplatform.controller;

import com.saasplatform.entity.Notification;
import com.saasplatform.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications(
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestParam Long userId) {
        
        try {
            List<Notification> notifications = notificationService.getUserNotifications(tenantId, userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            log.error("Error fetching notifications for user: {}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/unread")
    public ResponseEntity<List<Notification>> getUnreadNotifications(
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestParam Long userId) {
        
        try {
            List<Notification> notifications = notificationService.getUnreadNotifications(tenantId, userId);
            return ResponseEntity.ok(notifications);
        } catch (Exception e) {
            log.error("Error fetching unread notifications for user: {}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getUnreadNotificationCount(
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestParam Long userId) {
        
        try {
            long count = notificationService.getUnreadNotificationCount(tenantId, userId);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            log.error("Error fetching notification count for user: {}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable Long id) {
        
        try {
            Notification notification = notificationService.markAsRead(tenantId, id);
            if (notification != null) {
                return ResponseEntity.ok(notification);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error marking notification as read: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestParam Long userId) {
        
        try {
            notificationService.markAllAsRead(tenantId, userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error marking all notifications as read for user: {}", userId, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable Long id) {
        
        try {
            notificationService.deleteNotification(tenantId, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting notification: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}





