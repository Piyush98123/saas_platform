package com.saasplatform.service;

import com.saasplatform.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User findByEmail(String email);
    
    User save(User user);
    
    boolean existsByEmail(String email);
    
    List<User> getAllUsers(String tenantId);
    
    Optional<User> getUserById(String tenantId, Long id);
    
    User createUser(String tenantId, User user);
    
    User updateUser(String tenantId, Long id, User userDetails);
    
    void deleteUser(String tenantId, Long id);
    
    List<User> getUsersByRole(String tenantId, String role);
    
    List<User> searchUsers(String tenantId, String searchTerm);
    
    void changePassword(String tenantId, Long userId, String oldPassword, String newPassword);
    
    void resetPassword(String tenantId, Long userId, String newPassword);
    
    void updateUserStatus(String tenantId, Long userId, User.UserStatus status);
    
    long getTotalUserCount();
}

