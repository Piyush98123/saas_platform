package com.saasplatform.service.impl;

import com.saasplatform.entity.User;
import com.saasplatform.repository.UserRepository;
import com.saasplatform.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public List<User> getAllUsers(String tenantId) {
        return userRepository.findByTenantId(tenantId);
    }

    @Override
    public Optional<User> getUserById(String tenantId, Long id) {
        return userRepository.findById(id)
                .filter(user -> user.getTenantId().equals(tenantId));
    }

    @Override
    public User createUser(String tenantId, User user) {
        user.setTenantId(tenantId);
        user.setCreatedBy("system");
        user.setUpdatedBy("system");
        
        if (user.getStatus() == null) {
            user.setStatus(User.UserStatus.ACTIVE);
        }
        
        // Encode password if provided
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        return userRepository.save(user);
    }

    @Override
    public User updateUser(String tenantId, Long id, User userDetails) {
        User user = getUserById(tenantId, id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());
        user.setPhone(userDetails.getPhone());
        user.setStatus(userDetails.getStatus());
        user.setUpdatedBy("system");
        
        // Only update password if provided
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String tenantId, Long id) {
        User user = getUserById(tenantId, id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Override
    public List<User> getUsersByRole(String tenantId, String role) {
        return userRepository.findByTenantIdAndRolesName(tenantId, role);
    }

    @Override
    public List<User> searchUsers(String tenantId, String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllUsers(tenantId);
        }
        return userRepository.findByTenantIdAndSearchTerm(tenantId, searchTerm.trim());
    }

    @Override
    public void changePassword(String tenantId, Long userId, String oldPassword, String newPassword) {
        User user = getUserById(tenantId, userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Invalid old password");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedBy("system");
        userRepository.save(user);
    }

    @Override
    public void resetPassword(String tenantId, Long userId, String newPassword) {
        User user = getUserById(tenantId, userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setUpdatedBy("system");
        userRepository.save(user);
    }

    @Override
    public void updateUserStatus(String tenantId, Long userId, User.UserStatus status) {
        User user = getUserById(tenantId, userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setStatus(status);
        user.setUpdatedBy("system");
        userRepository.save(user);
    }
    
    @Override
    public long getTotalUserCount() {
        return userRepository.count();
    }
}

