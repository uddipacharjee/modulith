package com.incognito.modulith.users.repository;

import com.incognito.modulith.users.domain.UserEntity;
import com.incognito.modulith.users.domain.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    Optional<UserEntity> findByUsername(String username);
    
    Optional<UserEntity> findByEmail(String email);
    
    List<UserEntity> findByUserType(UserType userType);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}