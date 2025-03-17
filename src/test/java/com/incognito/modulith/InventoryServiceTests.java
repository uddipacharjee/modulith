package com.incognito.modulith;

import com.incognito.modulith.inventory.InventoryService;
import com.incognito.modulith.inventory.entity.InventoryEntity;
import com.incognito.modulith.inventory.exceptions.InsufficientStockException;
import com.incognito.modulith.inventory.exceptions.InventoryNotFoundException;
import com.incognito.modulith.inventory.repository.InventoryRepository;
import com.incognito.modulith.orders.domain.events.OrderCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTests {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    @Captor
    private ArgumentCaptor<InventoryEntity> inventoryCaptor;

    private static final String PRODUCT_CODE = "PROD-001";
    private static final int ORDER_QUANTITY = 5;
    private static final int AVAILABLE_STOCK = 10;

    private OrderCreatedEvent orderCreatedEvent;
    private InventoryEntity inventoryEntity;

    @BeforeEach
    void setUp() {
        orderCreatedEvent = new OrderCreatedEvent("ORD-123", PRODUCT_CODE, ORDER_QUANTITY);
        
        inventoryEntity = InventoryEntity.builder()
                .id(1L)
                .productCode(PRODUCT_CODE)
                .stockQuantity(AVAILABLE_STOCK)
                .build();
    }

    @Test
    @DisplayName("Should successfully update inventory when order is created")
    void shouldUpdateInventoryWhenOrderIsCreated() {
        // Arrange
        when(inventoryRepository.findByProductCode(PRODUCT_CODE)).thenReturn(Optional.of(inventoryEntity));
        when(inventoryRepository.save(any(InventoryEntity.class))).thenReturn(inventoryEntity);

        // Act
        inventoryService.on(orderCreatedEvent);

        // Assert
        verify(inventoryRepository).findByProductCode(PRODUCT_CODE);
        verify(inventoryRepository).save(inventoryCaptor.capture());
        
        InventoryEntity savedInventory = inventoryCaptor.getValue();
        assertThat(savedInventory.getStockQuantity()).isEqualTo(AVAILABLE_STOCK - ORDER_QUANTITY);
    }

    @Test
    @DisplayName("Should throw InventoryNotFoundException when product not found")
    void shouldThrowExceptionWhenProductNotFound() {
        // Arrange
        when(inventoryRepository.findByProductCode(PRODUCT_CODE)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> inventoryService.on(orderCreatedEvent))
                .isInstanceOf(InventoryNotFoundException.class)
                .hasMessageContaining(PRODUCT_CODE);
        
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw InsufficientStockException when not enough stock")
    void shouldThrowExceptionWhenInsufficientStock() {
        // Arrange
        inventoryEntity.setStockQuantity(ORDER_QUANTITY - 1); // Less than required
        when(inventoryRepository.findByProductCode(PRODUCT_CODE)).thenReturn(Optional.of(inventoryEntity));

        // Act & Assert
        assertThatThrownBy(() -> inventoryService.on(orderCreatedEvent))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining(PRODUCT_CODE);
        
        verify(inventoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle zero quantity orders correctly")
    void shouldHandleZeroQuantityOrders() {
        // Arrange
        OrderCreatedEvent zeroQuantityEvent = new OrderCreatedEvent("ORD-123", PRODUCT_CODE, 0);
        when(inventoryRepository.findByProductCode(PRODUCT_CODE)).thenReturn(Optional.of(inventoryEntity));
        when(inventoryRepository.save(any(InventoryEntity.class))).thenReturn(inventoryEntity);

        // Act
        inventoryService.on(zeroQuantityEvent);

        // Assert
        verify(inventoryRepository).save(inventoryCaptor.capture());
        InventoryEntity savedInventory = inventoryCaptor.getValue();
        assertThat(savedInventory.getStockQuantity()).isEqualTo(AVAILABLE_STOCK); // No change in stock
    }
}