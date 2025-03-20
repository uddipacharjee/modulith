package com.incognito.modulith.users.repository;

import com.incognito.modulith.users.domain.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
    
    Optional<CustomerEntity> findByUsername(String username);
    
    Optional<CustomerEntity> findByEmail(String email);
}