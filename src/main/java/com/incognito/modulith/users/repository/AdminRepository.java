package com.incognito.modulith.users.repository;

import com.incognito.modulith.users.domain.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    
    Optional<AdminEntity> findByUsername(String username);
    
    Optional<AdminEntity> findByEmail(String email);
    
    List<AdminEntity> findByDepartment(String department);
    
    List<AdminEntity> findByAdminLevel(Integer adminLevel);
}