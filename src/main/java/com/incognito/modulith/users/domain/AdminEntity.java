package com.incognito.modulith.users.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class AdminEntity extends UserEntity {

    @Column(name = "department")
    private String department;
    
    @Column(name = "admin_level")
    private Integer adminLevel;
    
    @Column(name = "access_all")
    private boolean accessAll;
    
    public AdminEntity(UserEntity user) {
        super(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(),
              user.getFirstName(), user.getLastName(), user.getPhoneNumber(), 
              UserType.ADMIN, user.getRoles(), user.isActive(),
              user.getCreatedAt(), user.getUpdatedAt());
    }
}