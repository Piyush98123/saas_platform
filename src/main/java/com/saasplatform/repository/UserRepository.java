package com.saasplatform.repository;

import com.saasplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    
    List<User> findByTenantId(String tenantId);
    
    Optional<User> findByTenantIdAndId(String tenantId, Long id);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE u.tenantId = :tenantId AND r.name = :roleName")
    List<User> findByTenantIdAndRolesName(@Param("tenantId") String tenantId, @Param("roleName") String roleName);
    
    @Query("SELECT u FROM User u WHERE u.tenantId = :tenantId AND " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<User> findByTenantIdAndSearchTerm(@Param("tenantId") String tenantId, @Param("searchTerm") String searchTerm);
}

