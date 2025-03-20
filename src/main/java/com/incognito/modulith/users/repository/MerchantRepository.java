package com.incognito.modulith.users.repository;

import com.incognito.modulith.users.domain.MerchantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<MerchantEntity, Long> {
    
    Optional<MerchantEntity> findByUsername(String username);
    
    Optional<MerchantEntity> findByEmail(String email);
    
    List<MerchantEntity> findByBusinessName(String businessName);
    
    List<MerchantEntity> findByVerificationStatus(String verificationStatus);
}