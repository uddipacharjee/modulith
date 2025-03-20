package com.incognito.modulith.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "merchants")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class MerchantEntity extends UserEntity {

    @Column(name = "business_name", nullable = false)
    private String businessName;
    
    @Column(name = "business_address")
    private String businessAddress;
    
    @Column(name = "tax_id")
    private String taxId;
    
    @Column(name = "verification_status")
    private String verificationStatus;
    
    @Column(name = "commission_rate")
    private Double commissionRate;
    
    public MerchantEntity(UserEntity user) {
        super(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(),
              user.getFirstName(), user.getLastName(), user.getPhoneNumber(), 
              UserType.MERCHANT, user.getRoles(), user.isActive(),
              user.getCreatedAt(), user.getUpdatedAt());
    }
}