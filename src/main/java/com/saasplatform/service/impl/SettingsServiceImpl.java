package com.saasplatform.service.impl;

import com.saasplatform.entity.SystemSettings;
import com.saasplatform.repository.SystemSettingsRepository;
import com.saasplatform.service.SettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SettingsServiceImpl implements SettingsService {

    private final SystemSettingsRepository settingsRepository;

    @Override
    public List<SystemSettings> getAllSettings(String tenantId) {
        return settingsRepository.findByTenantId(tenantId);
    }

    @Override
    public List<SystemSettings> getSettingsByCategory(String tenantId, String category) {
        return settingsRepository.findByTenantIdAndCategory(tenantId, category);
    }

    @Override
    public Optional<SystemSettings> getSettingByKey(String tenantId, String key) {
        return settingsRepository.findByTenantIdAndSettingKey(tenantId, key);
    }

    @Override
    public SystemSettings createSetting(String tenantId, SystemSettings setting) {
        setting.setTenantId(tenantId);
        setting.setCreatedBy("system");
        setting.setUpdatedBy("system");
        return settingsRepository.save(setting);
    }

    @Override
    public SystemSettings updateSetting(String tenantId, String key, SystemSettings settingDetails) {
        SystemSettings setting = getSettingByKey(tenantId, key)
                .orElseThrow(() -> new RuntimeException("Setting not found"));
        
        if (!setting.getIsEditable()) {
            throw new RuntimeException("Setting is not editable");
        }
        
        setting.setSettingValue(settingDetails.getSettingValue());
        setting.setUpdatedBy("system");
        
        return settingsRepository.save(setting);
    }

    @Override
    public void deleteSetting(String tenantId, String key) {
        SystemSettings setting = getSettingByKey(tenantId, key)
                .orElseThrow(() -> new RuntimeException("Setting not found"));
        
        if (!setting.getIsEditable()) {
            throw new RuntimeException("Setting is not editable");
        }
        
        settingsRepository.delete(setting);
    }

    @Override
    public String getSettingValue(String tenantId, String key, String defaultValue) {
        return getSettingByKey(tenantId, key)
                .map(SystemSettings::getSettingValue)
                .orElse(defaultValue);
    }

    @Override
    public Boolean getSettingValueAsBoolean(String tenantId, String key, Boolean defaultValue) {
        String value = getSettingValue(tenantId, key, null);
        if (value == null) return defaultValue;
        return Boolean.parseBoolean(value);
    }

    @Override
    public Integer getSettingValueAsInteger(String tenantId, String key, Integer defaultValue) {
        String value = getSettingValue(tenantId, key, null);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public void initializeDefaultSettings(String tenantId) {
        // Initialize default settings if none exist
        if (settingsRepository.countByTenantId(tenantId) == 0) {
            createDefaultSettings(tenantId);
        }
    }

    private void createDefaultSettings(String tenantId) {
        // General Settings
        createSetting(tenantId, createSetting("company.name", "Your Company Name", "STRING", "Company name", "GENERAL"));
        createSetting(tenantId, createSetting("company.website", "https://example.com", "STRING", "Company website", "GENERAL"));
        createSetting(tenantId, createSetting("company.email", "info@example.com", "STRING", "Company email", "GENERAL"));
        
        // Email Settings
        createSetting(tenantId, createSetting("email.smtp.host", "smtp.gmail.com", "STRING", "SMTP host", "EMAIL"));
        createSetting(tenantId, createSetting("email.smtp.port", "587", "NUMBER", "SMTP port", "EMAIL"));
        createSetting(tenantId, createSetting("email.smtp.ssl", "true", "BOOLEAN", "Use SSL", "EMAIL"));
        
        // Notification Settings
        createSetting(tenantId, createSetting("notifications.email.enabled", "true", "BOOLEAN", "Enable email notifications", "NOTIFICATIONS"));
        createSetting(tenantId, createSetting("notifications.sms.enabled", "false", "BOOLEAN", "Enable SMS notifications", "NOTIFICATIONS"));
        createSetting(tenantId, createSetting("notifications.push.enabled", "true", "BOOLEAN", "Enable push notifications", "NOTIFICATIONS"));
        
        // Security Settings
        createSetting(tenantId, createSetting("security.password.minLength", "8", "NUMBER", "Minimum password length", "SECURITY"));
        createSetting(tenantId, createSetting("security.session.timeout", "3600", "NUMBER", "Session timeout in seconds", "SECURITY"));
        createSetting(tenantId, createSetting("security.mfa.enabled", "false", "BOOLEAN", "Enable multi-factor authentication", "SECURITY"));
    }

    private SystemSettings createSetting(String key, String value, String type, String description, String category) {
        SystemSettings setting = new SystemSettings();
        setting.setSettingKey(key);
        setting.setSettingValue(value);
        setting.setSettingType(type);
        setting.setDescription(description);
        setting.setCategory(category);
        setting.setIsEditable(true);
        return setting;
    }
}





