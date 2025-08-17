package com.saasplatform.controller;

import com.saasplatform.entity.Company;
import com.saasplatform.entity.User;
import com.saasplatform.service.CompanyService;
import com.saasplatform.service.UserService;
import com.saasplatform.service.PermissionService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/super-admin")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class SuperAdminController {

    private final CompanyService companyService;
    private final UserService userService;
    private final PermissionService permissionService;

    /**
     * Get all companies (Super Admin only)
     */
    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies(
            @RequestParam String adminEmail) {
        
        try {
            // In a real app, you'd get the user from the security context
            User admin = userService.findByEmail(adminEmail);
            if (admin == null || !permissionService.isSuperAdmin(admin)) {
                return ResponseEntity.status(403).build();
            }

            List<Company> companies = companyService.getAllCompanies();
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            log.error("Error fetching all companies", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get company details (Super Admin only)
     */
    @GetMapping("/companies/{id}")
    public ResponseEntity<Company> getCompanyById(
            @PathVariable Long id,
            @RequestParam String adminEmail) {
        
        try {
            User admin = userService.findByEmail(adminEmail);
            if (admin == null || !permissionService.isSuperAdmin(admin)) {
                return ResponseEntity.status(403).build();
            }

            Optional<Company> companyOpt = companyService.getCompanyById(id);
            if (companyOpt.isPresent()) {
                return ResponseEntity.ok(companyOpt.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error fetching company: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Create new company (Super Admin only)
     */
    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(
            @RequestBody Company company,
            @RequestParam String adminEmail) {
        
        try {
            User admin = userService.findByEmail(adminEmail);
            if (admin == null || !permissionService.isSuperAdmin(admin)) {
                return ResponseEntity.status(403).build();
            }

            Company createdCompany = companyService.createCompany(company);
            return ResponseEntity.ok(createdCompany);
        } catch (Exception e) {
            log.error("Error creating company", e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Update company (Super Admin only)
     */
    @PutMapping("/companies/{id}")
    public ResponseEntity<Company> updateCompany(
            @PathVariable Long id,
            @RequestBody Company companyDetails,
            @RequestParam String adminEmail) {
        
        try {
            User admin = userService.findByEmail(adminEmail);
            if (admin == null || !permissionService.isSuperAdmin(admin)) {
                return ResponseEntity.status(403).build();
            }

            Company updatedCompany = companyService.updateCompany(id, companyDetails);
            if (updatedCompany != null) {
                return ResponseEntity.ok(updatedCompany);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error updating company: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Delete company (Super Admin only)
     */
    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(
            @PathVariable Long id,
            @RequestParam String adminEmail) {
        
        try {
            User admin = userService.findByEmail(adminEmail);
            if (admin == null || !permissionService.isSuperAdmin(admin)) {
                return ResponseEntity.status(403).build();
            }

            companyService.deleteCompany(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting company: {}", id, e);
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get all users across all companies (Super Admin only)
     */
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(
            @RequestParam String adminEmail) {
        
        try {
            User admin = userService.findByEmail(adminEmail);
            if (admin == null || !permissionService.isSuperAdmin(admin)) {
                return ResponseEntity.status(403).build();
            }

            List<User> users = userService.getAllUsers("all"); // Special tenant ID for super admin
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Error fetching all users", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get system overview (Super Admin only)
     */
    @GetMapping("/overview")
    public ResponseEntity<Map<String, Object>> getSystemOverview(
            @RequestParam String adminEmail) {
        
        try {
            User admin = userService.findByEmail(adminEmail);
            if (admin == null || !permissionService.isSuperAdmin(admin)) {
                return ResponseEntity.status(403).build();
            }

            // In a real app, you'd calculate these metrics
            Map<String, Object> overview = Map.of(
                "totalCompanies", companyService.getCompanyCount(),
                "totalUsers", userService.getTotalUserCount(),
                "activeCompanies", companyService.getActiveCompanyCount(),
                "systemStatus", "Healthy"
            );

            return ResponseEntity.ok(overview);
        } catch (Exception e) {
            log.error("Error fetching system overview", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Get company statistics (Super Admin only)
     */
    @GetMapping("/companies/{id}/stats")
    public ResponseEntity<Map<String, Object>> getCompanyStats(
            @PathVariable Long id,
            @RequestParam String adminEmail) {
        
        try {
            User admin = userService.findByEmail(adminEmail);
            if (admin == null || !permissionService.isSuperAdmin(admin)) {
                return ResponseEntity.status(403).build();
            }

            // In a real app, you'd calculate actual statistics
            Map<String, Object> stats = Map.of(
                "companyId", id,
                "totalUsers", 0,
                "totalCustomers", 0,
                "totalQuotes", 0,
                "totalBookings", 0,
                "monthlyRevenue", 0.0
            );

            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error fetching company stats: {}", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
