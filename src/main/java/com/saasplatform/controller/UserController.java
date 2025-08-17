package com.saasplatform.controller;

import com.saasplatform.entity.User;
import com.saasplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String role) {
        
        try {
            List<User> users;
            
            if (search != null && !search.trim().isEmpty()) {
                users = userService.searchUsers(tenantId, search);
            } else if (role != null && !role.trim().isEmpty()) {
                users = userService.getUsersByRole(tenantId, role);
            } else {
                users = userService.getAllUsers(tenantId);
            }
            
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching users", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable Long id) {
        
        try {
            return userService.getUserById(tenantId, id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching user with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<User> createUser(
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestBody User user) {
        
        try {
            User createdUser = userService.createUser(tenantId, user);
            return ResponseEntity.ok(createdUser);
        } catch (Exception e) {
            log.error("Error creating user", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable Long id,
            @RequestBody User user) {
        
        try {
            User updatedUser = userService.updateUser(tenantId, id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            log.error("Error updating user with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable Long id) {
        
        try {
            userService.deleteUser(tenantId, id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting user with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable Long id,
            @RequestBody PasswordChangeRequest request) {
        
        try {
            userService.changePassword(tenantId, id, request.getOldPassword(), request.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error changing password for user with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/{id}/reset-password")
    public ResponseEntity<Void> resetPassword(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable Long id,
            @RequestBody PasswordResetRequest request) {
        
        try {
            userService.resetPassword(tenantId, id, request.getNewPassword());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error resetting password for user with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateUserStatus(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable Long id,
            @RequestBody UserStatusUpdateRequest request) {
        
        try {
            userService.updateUserStatus(tenantId, id, request.getStatus());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error updating status for user with id: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    // DTOs for request bodies
    public static class PasswordChangeRequest {
        private String oldPassword;
        private String newPassword;

        public String getOldPassword() { return oldPassword; }
        public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }
        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    public static class PasswordResetRequest {
        private String newPassword;

        public String getNewPassword() { return newPassword; }
        public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
    }

    public static class UserStatusUpdateRequest {
        private User.UserStatus status;

        public User.UserStatus getStatus() { return status; }
        public void setStatus(User.UserStatus status) { this.status = status; }
    }
}





