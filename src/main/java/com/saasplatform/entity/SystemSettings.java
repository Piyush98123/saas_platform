package com.saasplatform.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "system_settings")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SystemSettings extends BaseEntity {

    @Column(name = "setting_key", nullable = false)
    private String settingKey;

    @Column(name = "setting_value", columnDefinition = "TEXT")
    private String settingValue;

    @Column(name = "setting_type")
    private String settingType; // STRING, NUMBER, BOOLEAN, JSON

    @Column(name = "description")
    private String description;

    @Column(name = "is_editable")
    private Boolean isEditable = true;

    @Column(name = "category")
    private String category; // GENERAL, EMAIL, NOTIFICATIONS, SECURITY, etc.
}





