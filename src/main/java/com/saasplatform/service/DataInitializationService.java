package com.saasplatform.service;

import com.saasplatform.entity.Company;
import com.saasplatform.entity.Customer;
import com.saasplatform.entity.Role;
import com.saasplatform.entity.SystemSettings;
import com.saasplatform.entity.User;
import com.saasplatform.security.Permissions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saasplatform.repository.CompanyRepository;
import com.saasplatform.repository.CustomerRepository;
import com.saasplatform.repository.NotificationRepository;
import com.saasplatform.repository.RoleRepository;
import com.saasplatform.repository.SystemSettingsRepository;
import com.saasplatform.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationService implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final NotificationRepository notificationRepository;
    private final SystemSettingsRepository systemSettingsRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        try {
            if (userRepository.count() == 0) {
                log.info("Initializing application data...");
                initializeData();
                log.info("Application data initialization completed successfully");
            } else {
                log.info("Application data already exists, skipping initialization");
            }
        } catch (Exception e) {
            log.error("Failed to initialize application data", e);
            // Don't throw the exception to prevent application startup failure
        }
    }

    private void initializeData() {
        // Create company
        Company company = new Company();
        company.setTenantId("default");
        company.setName("Default Company");
        company.setDomain("default.com");
        company.setEmail("admin@default.com");
        company.setSubdomain("default");
        company.setStatus(Company.CompanyStatus.ACTIVE);
        company.setPlan(Company.SubscriptionPlan.STARTER);
        company.setCreatedBy("system");
        company.setUpdatedBy("system");
        company = companyRepository.save(company);

        // Create roles
        Role superAdminRole = createRole("SUPER_ADMIN", "Super Administrator", Role.RoleType.SYSTEM);
        Role companyAdminRole = createRole("COMPANY_ADMIN", "Company Administrator", Role.RoleType.SYSTEM);
        Role staffRole = createRole("STAFF", "Staff Member", Role.RoleType.SYSTEM);

        // Create super admin user
        User superAdmin = new User();
        superAdmin.setTenantId("default");
        superAdmin.setEmail("superadmin@saasplatform.com");
        superAdmin.setPassword(passwordEncoder.encode("password123"));
        superAdmin.setFirstName("Super");
        superAdmin.setLastName("Admin");
        superAdmin.setCompany(company);
        superAdmin.setStatus(User.UserStatus.ACTIVE);
        superAdmin.setEmailVerified(true);
        superAdmin.setCreatedBy("system");
        superAdmin.setUpdatedBy("system");
        
        Set<Role> superAdminRoles = new HashSet<>();
        superAdminRoles.add(superAdminRole);
        superAdmin.setRoles(superAdminRoles);
        
        userRepository.save(superAdmin);
        
        // Create admin user
        User admin = new User();
        admin.setTenantId("default");
        admin.setEmail("admin@demoservice.com");
        admin.setPassword(passwordEncoder.encode("password123"));
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setCompany(company);
        admin.setStatus(User.UserStatus.ACTIVE);
        admin.setEmailVerified(true);
        admin.setCreatedBy("system");
        admin.setUpdatedBy("system");
        
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(companyAdminRole);
        admin.setRoles(adminRoles);
        
        userRepository.save(admin);
        
        // Create staff user
        User staff = new User();
        staff.setTenantId("default");
        staff.setEmail("staff@demoservice.com");
        staff.setPassword(passwordEncoder.encode("password123"));
        staff.setFirstName("Staff");
        staff.setLastName("User");
        staff.setCompany(company);
        staff.setStatus(User.UserStatus.ACTIVE);
        staff.setEmailVerified(true);
        staff.setCreatedBy("system");
        staff.setUpdatedBy("system");
        
        Set<Role> staffRoles = new HashSet<>();
        staffRoles.add(staffRole);
        staff.setRoles(staffRoles);
        
        userRepository.save(staff);
        
        // Create sample customers
        createSampleCustomers(company);
        
        // Create system settings
        createSystemSettings();
        
        log.info("Created users:");
        log.info("Super Admin: superadmin@saasplatform.com / password123");
        log.info("Admin: admin@demoservice.com / password123");
        log.info("Staff: staff@demoservice.com / password123");
    }

    private Role createRole(String name, String description, Role.RoleType type) {
        Role role = new Role();
        role.setTenantId("default");
        role.setName(name);
        role.setDescription(description);
        role.setType(type);
        
        // Set permissions based on role
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (name.equals(Role.SUPER_ADMIN)) {
                role.setPermissions(mapper.writeValueAsString(Permissions.SUPER_ADMIN_PERMISSIONS));
            } else if (name.equals(Role.COMPANY_ADMIN)) {
                role.setPermissions(mapper.writeValueAsString(Permissions.COMPANY_ADMIN_PERMISSIONS));
            } else if (name.equals(Role.STAFF)) {
                role.setPermissions(mapper.writeValueAsString(Permissions.STAFF_PERMISSIONS));
            }
        } catch (JsonProcessingException e) {
            log.error("Error setting permissions for role: {}", name, e);
        }
        
        role.setCreatedBy("system");
        role.setUpdatedBy("system");
        return roleRepository.save(role);
    }
    
    private void createSampleCustomers(Company company) {
        // Create sample customer 1
        Customer customer1 = new Customer();
        customer1.setTenantId("default");
        customer1.setCustomerNumber("CUST-2024-0001");
        customer1.setCompanyName("Tech Solutions Inc");
        customer1.setContactPerson("John Smith");
        customer1.setEmail("john@techsolutions.com");
        customer1.setPhone("+1-555-0101");
        customer1.setWebsite("https://techsolutions.com");
        customer1.setAddress("123 Tech Street");
        customer1.setCity("San Francisco");
        customer1.setState("CA");
        customer1.setCountry("USA");
        customer1.setZipCode("94105");
        customer1.setStatus(Customer.CustomerStatus.ACTIVE);
        customer1.setCustomerType(Customer.CustomerType.ENTERPRISE);
        customer1.setIndustry("Technology");
        customer1.setAnnualRevenue(new java.math.BigDecimal("5000000"));
        customer1.setEmployeeCount(250);
        customer1.setSource("Website");
        customer1.setNotes("Enterprise technology solutions provider");
        customer1.setCreatedBy("system");
        customer1.setUpdatedBy("system");
        customerRepository.save(customer1);
        
        // Create sample customer 2
        Customer customer2 = new Customer();
        customer2.setTenantId("default");
        customer2.setCustomerNumber("CUST-2024-0002");
        customer2.setCompanyName("Startup Ventures");
        customer2.setContactPerson("Sarah Johnson");
        customer2.setEmail("sarah@startupventures.com");
        customer2.setPhone("+1-555-0102");
        customer2.setWebsite("https://startupventures.com");
        customer2.setAddress("456 Innovation Ave");
        customer2.setCity("Austin");
        customer2.setState("TX");
        customer2.setCountry("USA");
        customer2.setZipCode("73301");
        customer2.setStatus(Customer.CustomerStatus.PROSPECT);
        customer2.setCustomerType(Customer.CustomerType.SMALL_BUSINESS);
        customer2.setIndustry("Finance");
        customer2.setAnnualRevenue(new java.math.BigDecimal("500000"));
        customer2.setEmployeeCount(15);
        customer2.setSource("Referral");
        customer2.setNotes("Fintech startup looking for growth solutions");
        customer2.setCreatedBy("system");
        customer2.setUpdatedBy("system");
        customerRepository.save(customer2);
        
        log.info("Created sample customers");
    }
    
    private void createSystemSettings() {
        // General Settings
        createSetting("company.name", "Your Company Name", "STRING", "Company name", "GENERAL");
        createSetting("company.website", "https://example.com", "STRING", "Company website", "GENERAL");
        createSetting("company.email", "info@example.com", "STRING", "Company email", "GENERAL");
        
        // Email Settings
        createSetting("email.smtp.host", "smtp.gmail.com", "STRING", "SMTP host", "EMAIL");
        createSetting("email.smtp.port", "587", "NUMBER", "SMTP port", "EMAIL");
        createSetting("email.smtp.ssl", "true", "BOOLEAN", "Use SSL", "EMAIL");
        
        // Notification Settings
        createSetting("notifications.email.enabled", "true", "BOOLEAN", "Enable email notifications", "NOTIFICATIONS");
        createSetting("notifications.sms.enabled", "false", "BOOLEAN", "Enable SMS notifications", "NOTIFICATIONS");
        createSetting("notifications.push.enabled", "true", "BOOLEAN", "Enable push notifications", "NOTIFICATIONS");
        
        // Security Settings
        createSetting("security.password.minLength", "8", "NUMBER", "Minimum password length", "SECURITY");
        createSetting("security.session.timeout", "3600", "NUMBER", "Session timeout in seconds", "SECURITY");
        createSetting("security.mfa.enabled", "false", "BOOLEAN", "Enable multi-factor authentication", "SECURITY");
        
        log.info("Created system settings");
    }
    
    private SystemSettings createSetting(String key, String value, String type, String description, String category) {
        SystemSettings setting = new SystemSettings();
        setting.setTenantId("default");
        setting.setSettingKey(key);
        setting.setSettingValue(value);
        setting.setSettingType(type);
        setting.setDescription(description);
        setting.setCategory(category);
        setting.setIsEditable(true);
        setting.setCreatedBy("system");
        setting.setUpdatedBy("system");
        return systemSettingsRepository.save(setting);
    }
}
