package com.incognito.modulith.users.service;

import com.incognito.modulith.users.domain.MerchantEntity;
import com.incognito.modulith.users.domain.UserEntity;
import com.incognito.modulith.users.domain.UserType;
import com.incognito.modulith.users.dto.MerchantDto;
import com.incognito.modulith.users.dto.UserDto;
import com.incognito.modulith.users.exceptions.UserNotFoundException;
import com.incognito.modulith.users.repository.MerchantRepository;
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
public class MerchantService {

    private final MerchantRepository merchantRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public MerchantDto findById(Long id) {
        return merchantRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new UserNotFoundException("Merchant not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<MerchantDto> findAll() {
        return merchantRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MerchantDto> findByBusinessName(String businessName) {
        return merchantRepository.findByBusinessName(businessName).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MerchantDto> findByVerificationStatus(String verificationStatus) {
        return merchantRepository.findByVerificationStatus(verificationStatus).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MerchantDto createMerchant(MerchantDto merchantDto) {
        // Set user type to MERCHANT
        merchantDto.setUserType(UserType.MERCHANT);
        
        // Add default merchant role if not present
        if (merchantDto.getRoles() == null || merchantDto.getRoles().isEmpty()) {
            merchantDto.getRoles().add("ROLE_MERCHANT");
        }
        
        // First create the base user
        UserDto createdUser = userService.createUser(merchantDto);
        
        // Then create the merchant with additional fields
        UserEntity userEntity = userRepository.findById(createdUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + createdUser.getId()));
        
        MerchantEntity merchantEntity = new MerchantEntity(userEntity);
        merchantEntity.setBusinessName(merchantDto.getBusinessName());
        merchantEntity.setBusinessAddress(merchantDto.getBusinessAddress());
        merchantEntity.setTaxId(merchantDto.getTaxId());
        merchantEntity.setVerificationStatus(merchantDto.getVerificationStatus());
        merchantEntity.setCommissionRate(merchantDto.getCommissionRate());
        
        MerchantEntity savedMerchant = merchantRepository.save(merchantEntity);
        log.info("Created new merchant with id: {}", savedMerchant.getId());
        
        return mapToDto(savedMerchant);
    }

    @Transactional
    public MerchantDto updateMerchant(Long id, MerchantDto merchantDto) {
        MerchantEntity existingMerchant = merchantRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Merchant not found with id: " + id));
        
        // Update base user fields
        UserDto userDto = new UserDto();
        userDto.setFirstName(merchantDto.getFirstName());
        userDto.setLastName(merchantDto.getLastName());
        userDto.setPhoneNumber(merchantDto.getPhoneNumber());
        userDto.setActive(merchantDto.isActive());
        userDto.setPassword(merchantDto.getPassword());
        userDto.setRoles(merchantDto.getRoles());
        
        userService.updateUser(id, userDto);
        
        // Update merchant-specific fields
        existingMerchant.setBusinessName(merchantDto.getBusinessName());
        existingMerchant.setBusinessAddress(merchantDto.getBusinessAddress());
        existingMerchant.setTaxId(merchantDto.getTaxId());
        existingMerchant.setVerificationStatus(merchantDto.getVerificationStatus());
        existingMerchant.setCommissionRate(merchantDto.getCommissionRate());
        
        MerchantEntity updatedMerchant = merchantRepository.save(existingMerchant);
        log.info("Updated merchant with id: {}", updatedMerchant.getId());
        
        return mapToDto(updatedMerchant);
    }

    // Helper methods for mapping between entity and DTO
    private MerchantDto mapToDto(MerchantEntity merchantEntity) {
        MerchantDto merchantDto = new MerchantDto();
        merchantDto.setId(merchantEntity.getId());
        merchantDto.setUsername(merchantEntity.getUsername());
        merchantDto.setEmail(merchantEntity.getEmail());
        merchantDto.setFirstName(merchantEntity.getFirstName());
        merchantDto.setLastName(merchantEntity.getLastName());
        merchantDto.setPhoneNumber(merchantEntity.getPhoneNumber());
        merchantDto.setUserType(merchantEntity.getUserType());
        merchantDto.setRoles(merchantEntity.getRoles());
        merchantDto.setActive(merchantEntity.isActive());
        merchantDto.setCreatedAt(merchantEntity.getCreatedAt());
        merchantDto.setUpdatedAt(merchantEntity.getUpdatedAt());
        merchantDto.setBusinessName(merchantEntity.getBusinessName());
        merchantDto.setBusinessAddress(merchantEntity.getBusinessAddress());
        merchantDto.setTaxId(merchantEntity.getTaxId());
        merchantDto.setVerificationStatus(merchantEntity.getVerificationStatus());
        merchantDto.setCommissionRate(merchantEntity.getCommissionRate());
        return merchantDto;
    }
}