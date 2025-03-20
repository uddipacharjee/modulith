package com.incognito.modulith.inventory;

import com.incognito.modulith.inventory.entity.InventoryEntity;
import com.incognito.modulith.inventory.exceptions.InsufficientStockException;
import com.incognito.modulith.inventory.exceptions.InventoryNotFoundException;
import com.incognito.modulith.inventory.repository.InventoryRepository;
import com.incognito.modulith.orders.domain.events.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @EventListener
    @Transactional(readOnly = true)
    public void on(OrderCreatedEvent event) {
        log.info("Updating inventory for product code: {}, quantity: {}",
                event.productCode(), event.quantity());

        InventoryEntity inventory = inventoryRepository.findByProductCode(event.productCode())
                .orElseThrow(() -> new InventoryNotFoundException(
                        "Inventory not found for product: " + event.productCode()));

        try {
            inventory.reduceStock(event.quantity());
            inventoryRepository.save(inventory);
            log.info("Successfully updated inventory for product: {}. New stock level: {}",
                    event.productCode(), inventory.getStockQuantity());
        } catch (InsufficientStockException e) {
            log.error("Failed to update inventory: {}", e.getMessage());
            // In a real application, you might want to handle this differently,
            // such as publishing a compensating event to cancel the order
            throw e;
        }
    }
}