package com.saasplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@Table(name = "roles")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "permissions", columnDefinition = "TEXT")
    private String permissions; // JSON string of permissions

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private RoleType type = RoleType.CUSTOM;

    public enum RoleType {
        SYSTEM, CUSTOM
    }

    // Predefined system roles
    public static final String SUPER_ADMIN = "SUPER_ADMIN";
    public static final String COMPANY_ADMIN = "COMPANY_ADMIN";
    public static final String STAFF = "STAFF";
    public static final String CUSTOMER = "CUSTOMER";
}

