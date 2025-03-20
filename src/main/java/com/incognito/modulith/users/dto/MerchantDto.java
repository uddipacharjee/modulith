package com.incognito.modulith.users.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class MerchantDto extends UserDto {
    
    private String businessName;
    
    private String businessAddress;
    
    private String taxId;
    
    private String verificationStatus;
    
    private Double commissionRate;
}