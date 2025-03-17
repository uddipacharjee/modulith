package com.incognito.modulith;

import com.incognito.modulith.ModulithApplication;
import com.incognito.modulith.inventory.entity.InventoryEntity;
import com.incognito.modulith.inventory.exceptions.InsufficientStockException;
import com.incognito.modulith.inventory.repository.InventoryRepository;
import com.incognito.modulith.orders.domain.events.OrderCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.modulith.test.ApplicationModuleTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(classes = ModulithApplication.class)
@ApplicationModuleTest
@Transactional
class InventoryIntegrationTests {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private InventoryRepository inventoryRepository;

    private static final String PRODUCT_CODE = "TEST-PRODUCT";
    private static final int INITIAL_STOCK = 10;
    private static final int ORDER_QUANTITY = 3;

    @BeforeEach
    void setUp() {
        // Create test inventory
        InventoryEntity inventory = InventoryEntity.builder()
                .productCode(PRODUCT_CODE)
                .stockQuantity(INITIAL_STOCK)
                .build();
        inventoryRepository.save(inventory);
    }

    @Test
    @DisplayName("Should update inventory when OrderCreatedEvent is published")
    void shouldUpdateInventoryWhenOrderCreatedEventIsPublished() {
        // Arrange
        OrderCreatedEvent event = new OrderCreatedEvent("ORD-TEST-123", PRODUCT_CODE, ORDER_QUANTITY);

        // Act
        eventPublisher.publishEvent(event);

        // Assert
        Optional<InventoryEntity> updatedInventory = inventoryRepository.findByProductCode(PRODUCT_CODE);
        assertThat(updatedInventory).isPresent();
        assertThat(updatedInventory.get().getStockQuantity()).isEqualTo(INITIAL_STOCK - ORDER_QUANTITY);
    }

    @Test
    @DisplayName("Should throw exception when order quantity exceeds available stock")
    void shouldThrowExceptionWhenOrderQuantityExceedsAvailableStock() {
        // Arrange
        OrderCreatedEvent event = new OrderCreatedEvent("ORD-TEST-456", PRODUCT_CODE, INITIAL_STOCK + 1);

        // Act & Assert
        assertThatThrownBy(() -> eventPublisher.publishEvent(event))
                .isInstanceOf(InsufficientStockException.class)
                .hasMessageContaining(PRODUCT_CODE);

        // Verify stock wasn't changed
        Optional<InventoryEntity> inventory = inventoryRepository.findByProductCode(PRODUCT_CODE);
        assertThat(inventory).isPresent();
        assertThat(inventory.get().getStockQuantity()).isEqualTo(INITIAL_STOCK);
    }

    @Test
    @DisplayName("Should handle multiple order events correctly")
    void shouldHandleMultipleOrderEventsCorrectly() {
        // Arrange
        OrderCreatedEvent event1 = new OrderCreatedEvent("ORD-TEST-789", PRODUCT_CODE, 2);
        OrderCreatedEvent event2 = new OrderCreatedEvent("ORD-TEST-790", PRODUCT_CODE, 3);

        // Act
        eventPublisher.publishEvent(event1);
        eventPublisher.publishEvent(event2);

        // Assert
        Optional<InventoryEntity> updatedInventory = inventoryRepository.findByProductCode(PRODUCT_CODE);
        assertThat(updatedInventory).isPresent();
        assertThat(updatedInventory.get().getStockQuantity()).isEqualTo(INITIAL_STOCK - 2 - 3);
    }
}