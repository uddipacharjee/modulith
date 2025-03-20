package com.incognito.modulith.users.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class CustomerDto extends UserDto {
    
    private String shippingAddress;
    
    private String billingAddress;
    
    private String preferredPaymentMethod;
}