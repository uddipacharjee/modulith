package com.incognito.modulith.users.service;

import com.incognito.modulith.users.domain.AdminEntity;
import com.incognito.modulith.users.domain.UserEntity;
import com.incognito.modulith.users.domain.UserType;
import com.incognito.modulith.users.dto.AdminDto;
import com.incognito.modulith.users.dto.UserDto;
import com.incognito.modulith.users.exceptions.UserNotFoundException;
import com.incognito.modulith.users.repository.AdminRepository;
import com.incognito.modulith.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public AdminDto findById(Long id) {
        return adminRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new UserNotFoundException("Admin not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<AdminDto> findAll() {
        return adminRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminDto> findByDepartment(String department) {
        return adminRepository.findByDepartment(department).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdminDto> findByAdminLevel(Integer adminLevel) {
        return adminRepository.findByAdminLevel(adminLevel).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdminDto createAdmin(AdminDto adminDto) {
        // Set user type to ADMIN
        adminDto.setUserType(UserType.ADMIN);
        
        // Add default admin role if not present
        if (adminDto.getRoles() == null || adminDto.getRoles().isEmpty()) {
            adminDto.getRoles().add("ROLE_ADMIN");
        }
        
        // First create the base user
        UserDto createdUser = userService.createUser(adminDto);
        
        // Then create the admin with additional fields
        UserEntity userEntity = userRepository.findById(createdUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + createdUser.getId()));
        
        AdminEntity adminEntity = new AdminEntity(userEntity);
        adminEntity.setDepartment(adminDto.getDepartment());
        adminEntity.setAdminLevel(adminDto.getAdminLevel());
        adminEntity.setAccessAll(adminDto.isAccessAll());
        
        AdminEntity savedAdmin = adminRepository.save(adminEntity);
        log.info("Created new admin with id: {}", savedAdmin.getId());
        
        return mapToDto(savedAdmin);
    }

    @Transactional
    public AdminDto updateAdmin(Long id, AdminDto adminDto) {
        AdminEntity existingAdmin = adminRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Admin not found with id: " + id));
        
        // Update base user fields
        UserDto userDto = new UserDto();
        userDto.setFirstName(adminDto.getFirstName());
        userDto.setLastName(adminDto.getLastName());
        userDto.setPhoneNumber(adminDto.getPhoneNumber());
        userDto.setActive(adminDto.isActive());
        userDto.setPassword(adminDto.getPassword());
        userDto.setRoles(adminDto.getRoles());
        
        userService.updateUser(id, userDto);
        
        // Update admin-specific fields
        existingAdmin.setDepartment(adminDto.getDepartment());
        existingAdmin.setAdminLevel(adminDto.getAdminLevel());
        existingAdmin.setAccessAll(adminDto.isAccessAll());
        
        AdminEntity updatedAdmin = adminRepository.save(existingAdmin);
        log.info("Updated admin with id: {}", updatedAdmin.getId());
        
        return mapToDto(updatedAdmin);
    }

    // Helper methods for mapping between entity and DTO
    private AdminDto mapToDto(AdminEntity adminEntity) {
        AdminDto adminDto = new AdminDto();
        adminDto.setId(adminEntity.getId());
        adminDto.setUsername(adminEntity.getUsername());
        adminDto.setEmail(adminEntity.getEmail());
        adminDto.setFirstName(adminEntity.getFirstName());
        adminDto.setLastName(adminEntity.getLastName());
        adminDto.setPhoneNumber(adminEntity.getPhoneNumber());
        adminDto.setUserType(adminEntity.getUserType());
        adminDto.setRoles(adminEntity.getRoles());
        adminDto.setActive(adminEntity.isActive());
        adminDto.setCreatedAt(adminEntity.getCreatedAt());
        adminDto.setUpdatedAt(adminEntity.getUpdatedAt());
        adminDto.setDepartment(adminEntity.getDepartment());
        adminDto.setAdminLevel(adminEntity.getAdminLevel());
        adminDto.setAccessAll(adminEntity.isAccessAll());
        return adminDto;
    }
}