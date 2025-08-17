package com.saasplatform.controller;

import com.saasplatform.entity.SystemSettings;
import com.saasplatform.service.SettingsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/settings")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class SettingsController {

    private final SettingsService settingsService;

    @GetMapping
    public ResponseEntity<List<SystemSettings>> getAllSettings(
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestParam(required = false) String category) {
        
        try {
            List<SystemSettings> settings;
            if (category != null && !category.trim().isEmpty()) {
                settings = settingsService.getSettingsByCategory(tenantId, category);
            } else {
                settings = settingsService.getAllSettings(tenantId);
            }
            return ResponseEntity.ok(settings);
        } catch (Exception e) {
            log.error("Error fetching settings", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{key}")
    public ResponseEntity<SystemSettings> getSettingByKey(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable String key) {
        
        try {
            return settingsService.getSettingByKey(tenantId, key)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching setting with key: {}", key, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping
    public ResponseEntity<SystemSettings> createSetting(
            @RequestParam(defaultValue = "default") String tenantId,
            @RequestBody SystemSettings setting) {
        
        try {
            SystemSettings createdSetting = settingsService.createSetting(tenantId, setting);
            return ResponseEntity.ok(createdSetting);
        } catch (Exception e) {
            log.error("Error creating setting", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{key}")
    public ResponseEntity<SystemSettings> updateSetting(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable String key,
            @RequestBody SystemSettings setting) {
        
        try {
            SystemSettings updatedSetting = settingsService.updateSetting(tenantId, key, setting);
            return ResponseEntity.ok(updatedSetting);
        } catch (Exception e) {
            log.error("Error updating setting with key: {}", key, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/{key}")
    public ResponseEntity<Void> deleteSetting(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable String key) {
        
        try {
            settingsService.deleteSetting(tenantId, key);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error deleting setting with key: {}", key, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/initialize")
    public ResponseEntity<Void> initializeDefaultSettings(
            @RequestParam(defaultValue = "default") String tenantId) {
        
        try {
            settingsService.initializeDefaultSettings(tenantId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error("Error initializing default settings", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/value/{key}")
    public ResponseEntity<String> getSettingValue(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable String key,
            @RequestParam(required = false) String defaultValue) {
        
        try {
            String value = settingsService.getSettingValue(tenantId, key, defaultValue);
            return ResponseEntity.ok(value);
        } catch (Exception e) {
            log.error("Error fetching setting value for key: {}", key, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/boolean/{key}")
    public ResponseEntity<Boolean> getSettingValueAsBoolean(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable String key,
            @RequestParam(defaultValue = "false") Boolean defaultValue) {
        
        try {
            Boolean value = settingsService.getSettingValueAsBoolean(tenantId, key, defaultValue);
            return ResponseEntity.ok(value);
        } catch (Exception e) {
            log.error("Error fetching boolean setting value for key: {}", key, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/integer/{key}")
    public ResponseEntity<Integer> getSettingValueAsInteger(
            @RequestParam(defaultValue = "default") String tenantId,
            @PathVariable String key,
            @RequestParam(defaultValue = "0") Integer defaultValue) {
        
        try {
            Integer value = settingsService.getSettingValueAsInteger(tenantId, key, defaultValue);
            return ResponseEntity.ok(value);
        } catch (Exception e) {
            log.error("Error fetching integer setting value for key: {}", key, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}





