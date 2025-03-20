package com.incognito.modulith.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CustomerEntity extends UserEntity {

    @Column(name = "shipping_address")
    private String shippingAddress;

    @Column(name = "billing_address")
    private String billingAddress;
    
    @Column(name = "preferred_payment_method")
    private String preferredPaymentMethod;
    
    public CustomerEntity(UserEntity user) {
        super(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(),
              user.getFirstName(), user.getLastName(), user.getPhoneNumber(), 
              UserType.CUSTOMER, user.getRoles(), user.isActive(),
              user.getCreatedAt(), user.getUpdatedAt());
    }
}