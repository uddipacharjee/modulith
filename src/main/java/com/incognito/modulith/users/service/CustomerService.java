package com.incognito.modulith.users.service;

import com.incognito.modulith.users.domain.CustomerEntity;
import com.incognito.modulith.users.domain.UserEntity;
import com.incognito.modulith.users.domain.UserType;
import com.incognito.modulith.users.dto.CustomerDto;
import com.incognito.modulith.users.dto.UserDto;
import com.incognito.modulith.users.exceptions.UserNotFoundException;
import com.incognito.modulith.users.repository.CustomerRepository;
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
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    @Transactional(readOnly = true)
    public CustomerDto findById(Long id) {
        return customerRepository.findById(id)
                .map(this::mapToDto)
                .orElseThrow(() -> new UserNotFoundException("Customer not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> findAll() {
        return customerRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        // Set user type to CUSTOMER
        customerDto.setUserType(UserType.CUSTOMER);
        
        // Add default customer role if not present
        if (customerDto.getRoles() == null || customerDto.getRoles().isEmpty()) {
            customerDto.getRoles().add("ROLE_CUSTOMER");
        }
        
        // First create the base user
        UserDto createdUser = userService.createUser(customerDto);
        
        // Then create the customer with additional fields
        UserEntity userEntity = userRepository.findById(createdUser.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + createdUser.getId()));
        
        CustomerEntity customerEntity = new CustomerEntity(userEntity);
        customerEntity.setShippingAddress(customerDto.getShippingAddress());
        customerEntity.setBillingAddress(customerDto.getBillingAddress());
        customerEntity.setPreferredPaymentMethod(customerDto.getPreferredPaymentMethod());
        
        CustomerEntity savedCustomer = customerRepository.save(customerEntity);
        log.info("Created new customer with id: {}", savedCustomer.getId());
        
        return mapToDto(savedCustomer);
    }

    @Transactional
    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        CustomerEntity existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Customer not found with id: " + id));
        
        // Update base user fields
        UserDto userDto = new UserDto();
        userDto.setFirstName(customerDto.getFirstName());
        userDto.setLastName(customerDto.getLastName());
        userDto.setPhoneNumber(customerDto.getPhoneNumber());
        userDto.setActive(customerDto.isActive());
        userDto.setPassword(customerDto.getPassword());
        userDto.setRoles(customerDto.getRoles());
        
        userService.updateUser(id, userDto);
        
        // Update customer-specific fields
        existingCustomer.setShippingAddress(customerDto.getShippingAddress());
        existingCustomer.setBillingAddress(customerDto.getBillingAddress());
        existingCustomer.setPreferredPaymentMethod(customerDto.getPreferredPaymentMethod());
        
        CustomerEntity updatedCustomer = customerRepository.save(existingCustomer);
        log.info("Updated customer with id: {}", updatedCustomer.getId());
        
        return mapToDto(updatedCustomer);
    }

    // Helper methods for mapping between entity and DTO
    private CustomerDto mapToDto(CustomerEntity customerEntity) {
        CustomerDto customerDto = new CustomerDto();
        customerDto.setId(customerEntity.getId());
        customerDto.setUsername(customerEntity.getUsername());
        customerDto.setEmail(customerEntity.getEmail());
        customerDto.setFirstName(customerEntity.getFirstName());
        customerDto.setLastName(customerEntity.getLastName());
        customerDto.setPhoneNumber(customerEntity.getPhoneNumber());
        customerDto.setUserType(customerEntity.getUserType());
        customerDto.setRoles(customerEntity.getRoles());
        customerDto.setActive(customerEntity.isActive());
        customerDto.setCreatedAt(customerEntity.getCreatedAt());
        customerDto.setUpdatedAt(customerEntity.getUpdatedAt());
        customerDto.setShippingAddress(customerEntity.getShippingAddress());
        customerDto.setBillingAddress(customerEntity.getBillingAddress());
        customerDto.setPreferredPaymentMethod(customerEntity.getPreferredPaymentMethod());
        return customerDto;
    }
}