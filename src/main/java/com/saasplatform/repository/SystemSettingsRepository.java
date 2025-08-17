package com.saasplatform.repository;

import com.saasplatform.entity.SystemSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemSettingsRepository extends JpaRepository<SystemSettings, Long> {

    List<SystemSettings> findByTenantId(String tenantId);
    
    Optional<SystemSettings> findByTenantIdAndSettingKey(String tenantId, String settingKey);
    
    List<SystemSettings> findByTenantIdAndCategory(String tenantId, String category);
    
    boolean existsByTenantIdAndSettingKey(String tenantId, String settingKey);
    
    long countByTenantId(String tenantId);
}
