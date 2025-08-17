package com.saasplatform.security;

import java.util.Arrays;
import java.util.List;

public class Permissions {
    
    // Super Admin Permissions (Global)
    public static final String VIEW_ALL_COMPANIES = "VIEW_ALL_COMPANIES";
    public static final String MANAGE_ALL_COMPANIES = "MANAGE_ALL_COMPANIES";
    public static final String VIEW_ALL_USERS = "VIEW_ALL_USERS";
    public static final String MANAGE_ALL_USERS = "MANAGE_ALL_USERS";
    public static final String VIEW_ALL_REVENUE = "VIEW_ALL_REVENUE";
    public static final String SYSTEM_SETTINGS = "SYSTEM_SETTINGS";
    
    // Company Admin Permissions (Tenant-scoped)
    public static final String VIEW_COMPANY_DASHBOARD = "VIEW_COMPANY_DASHBOARD";
    public static final String MANAGE_COMPANY_USERS = "MANAGE_COMPANY_USERS";
    public static final String VIEW_COMPANY_REVENUE = "VIEW_COMPANY_REVENUE";
    public static final String MANAGE_COMPANY_SETTINGS = "MANAGE_COMPANY_SETTINGS";
    public static final String APPROVE_QUOTES = "APPROVE_QUOTES";
    public static final String APPROVE_BOOKINGS = "APPROVE_BOOKINGS";
    public static final String VIEW_NOTIFICATIONS = "VIEW_NOTIFICATIONS";
    
    // Staff Permissions (Tenant-scoped)
    public static final String CREATE_QUOTES = "CREATE_QUOTES";
    public static final String EDIT_QUOTES = "EDIT_QUOTES";
    public static final String VIEW_QUOTES = "VIEW_QUOTES";
    public static final String CREATE_BOOKINGS = "CREATE_BOOKINGS";
    public static final String EDIT_BOOKINGS = "EDIT_BOOKINGS";
    public static final String VIEW_BOOKINGS = "VIEW_BOOKINGS";
    public static final String CREATE_LEADS = "CREATE_LEADS";
    public static final String EDIT_LEADS = "EDIT_LEADS";
    public static final String VIEW_LEADS = "VIEW_LEADS";
    public static final String CREATE_CUSTOMERS = "CREATE_CUSTOMERS";
    public static final String EDIT_CUSTOMERS = "EDIT_CUSTOMERS";
    public static final String VIEW_CUSTOMERS = "VIEW_CUSTOMERS";
    
    // Permission Groups
    public static final List<String> SUPER_ADMIN_PERMISSIONS = Arrays.asList(
        VIEW_ALL_COMPANIES, MANAGE_ALL_COMPANIES, VIEW_ALL_USERS, MANAGE_ALL_USERS,
        VIEW_ALL_REVENUE, SYSTEM_SETTINGS
    );
    
    public static final List<String> COMPANY_ADMIN_PERMISSIONS = Arrays.asList(
        VIEW_COMPANY_DASHBOARD, MANAGE_COMPANY_USERS, VIEW_COMPANY_REVENUE,
        MANAGE_COMPANY_SETTINGS, APPROVE_QUOTES, APPROVE_BOOKINGS, VIEW_NOTIFICATIONS
    );
    
    public static final List<String> STAFF_PERMISSIONS = Arrays.asList(
        CREATE_QUOTES, EDIT_QUOTES, VIEW_QUOTES,
        CREATE_BOOKINGS, EDIT_BOOKINGS, VIEW_BOOKINGS,
        CREATE_LEADS, EDIT_LEADS, VIEW_LEADS,
        CREATE_CUSTOMERS, EDIT_CUSTOMERS, VIEW_CUSTOMERS
    );
    
    // Helper method to check if a permission is valid
    public static boolean isValidPermission(String permission) {
        return SUPER_ADMIN_PERMISSIONS.contains(permission) ||
               COMPANY_ADMIN_PERMISSIONS.contains(permission) ||
               STAFF_PERMISSIONS.contains(permission);
    }
}





