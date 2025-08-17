package com.saasplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "companies")
@EqualsAndHashCode(callSuper = true)
public class Company extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "domain", unique = true)
    private String domain;

    @Column(name = "subdomain", unique = true)
    private String subdomain;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "country")
    private String country;

    @Column(name = "logo_url")
    private String logoUrl;

    @Column(name = "website")
    private String website;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CompanyStatus status = CompanyStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false)
    private SubscriptionPlan plan = SubscriptionPlan.STARTER;

    @Column(name = "plan_start_date")
    private LocalDateTime planStartDate;

    @Column(name = "plan_end_date")
    private LocalDateTime planEndDate;

    @Column(name = "max_users")
    private Integer maxUsers = 5;

    @Column(name = "max_projects")
    private Integer maxProjects = 10;

    @Column(name = "features", columnDefinition = "TEXT")
    private String features; // JSON string of enabled features

    @Column(name = "settings", columnDefinition = "TEXT")
    private String settings; // JSON string of company settings

    public enum CompanyStatus {
        ACTIVE, SUSPENDED, CANCELLED, PENDING
    }

    public enum SubscriptionPlan {
        STARTER, PROFESSIONAL, ENTERPRISE, CUSTOM
    }
}

