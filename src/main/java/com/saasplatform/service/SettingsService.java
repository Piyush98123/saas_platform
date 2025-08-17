package com.saasplatform.service;

import com.saasplatform.entity.SystemSettings;

import java.util.List;
import java.util.Optional;

public interface SettingsService {

    List<SystemSettings> getAllSettings(String tenantId);
    
    List<SystemSettings> getSettingsByCategory(String tenantId, String category);
    
    Optional<SystemSettings> getSettingByKey(String tenantId, String key);
    
    SystemSettings createSetting(String tenantId, SystemSettings setting);
    
    SystemSettings updateSetting(String tenantId, String key, SystemSettings setting);
    
    void deleteSetting(String tenantId, String key);
    
    String getSettingValue(String tenantId, String key, String defaultValue);
    
    Boolean getSettingValueAsBoolean(String tenantId, String key, Boolean defaultValue);
    
    Integer getSettingValueAsInteger(String tenantId, String key, Integer defaultValue);
    
    void initializeDefaultSettings(String tenantId);
}



