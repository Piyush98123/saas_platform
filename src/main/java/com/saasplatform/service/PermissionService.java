package com.saasplatform.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saasplatform.entity.User;
import com.saasplatform.security.Permissions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {

    private final ObjectMapper objectMapper;

    /**
     * Check if a user has a specific permission
     */
    public boolean hasPermission(User user, String permission) {
        if (user == null || user.getRoles() == null) {
            return false;
        }

        return user.getRoles().stream()
                .anyMatch(role -> {
                    try {
                        List<String> rolePermissions = parsePermissions(role.getPermissions());
                        return rolePermissions.contains(permission);
                    } catch (Exception e) {
                        log.error("Error parsing permissions for role: {}", role.getName(), e);
                        return false;
                    }
                });
    }

    /**
     * Check if a user has any of the specified permissions
     */
    public boolean hasAnyPermission(User user, String... permissions) {
        for (String permission : permissions) {
            if (hasPermission(user, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if a user has all of the specified permissions
     */
    public boolean hasAllPermissions(User user, String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(user, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get all permissions for a user
     */
    public Set<String> getUserPermissions(User user) {
        if (user == null || user.getRoles() == null) {
            return Set.of();
        }

        return user.getRoles().stream()
                .flatMap(role -> {
                    try {
                        return parsePermissions(role.getPermissions()).stream();
                    } catch (Exception e) {
                        log.error("Error parsing permissions for role: {}", role.getName(), e);
                        return java.util.stream.Stream.<String>empty();
                    }
                })
                .collect(Collectors.toSet());
    }

    /**
     * Check if user is super admin
     */
    public boolean isSuperAdmin(User user) {
        return hasPermission(user, Permissions.VIEW_ALL_COMPANIES);
    }

    /**
     * Check if user is company admin
     */
    public boolean isCompanyAdmin(User user) {
        return hasPermission(user, Permissions.VIEW_COMPANY_DASHBOARD);
    }

    /**
     * Check if user can manage company users
     */
    public boolean canManageCompanyUsers(User user) {
        return hasPermission(user, Permissions.MANAGE_COMPANY_USERS);
    }

    /**
     * Check if user can approve quotes
     */
    public boolean canApproveQuotes(User user) {
        return hasPermission(user, Permissions.APPROVE_QUOTES);
    }

    /**
     * Check if user can approve bookings
     */
    public boolean canApproveBookings(User user) {
        return hasPermission(user, Permissions.APPROVE_BOOKINGS);
    }

    /**
     * Check if user can create quotes
     */
    public boolean canCreateQuotes(User user) {
        return hasPermission(user, Permissions.CREATE_QUOTES);
    }

    /**
     * Check if user can create bookings
     */
    public boolean canCreateBookings(User user) {
        return hasPermission(user, Permissions.CREATE_BOOKINGS);
    }

    /**
     * Check if user can view company revenue
     */
    public boolean canViewCompanyRevenue(User user) {
        return hasPermission(user, Permissions.VIEW_COMPANY_REVENUE);
    }

    /**
     * Check if user can view all companies (super admin only)
     */
    public boolean canViewAllCompanies(User user) {
        return hasPermission(user, Permissions.VIEW_ALL_COMPANIES);
    }

    /**
     * Parse permissions JSON string to List
     */
    private List<String> parsePermissions(String permissionsJson) throws JsonProcessingException {
        if (permissionsJson == null || permissionsJson.trim().isEmpty()) {
            return List.of();
        }
        return objectMapper.readValue(permissionsJson, new TypeReference<List<String>>() {});
    }

    /**
     * Validate if a permission string is valid
     */
    public boolean isValidPermission(String permission) {
        return Permissions.isValidPermission(permission);
    }
}



