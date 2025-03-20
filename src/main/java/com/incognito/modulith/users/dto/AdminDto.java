package com.incognito.modulith.users.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
public class AdminDto extends UserDto {
    
    private String department;
    
    private Integer adminLevel;
    
    private boolean accessAll;
}