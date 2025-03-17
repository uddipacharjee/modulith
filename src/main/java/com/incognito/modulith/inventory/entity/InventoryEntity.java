package com.incognito.modulith.inventory.entity;

import com.incognito.modulith.inventory.exceptions.InsufficientStockException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String productCode;
    
    @Column(nullable = false)
    private Integer stockQuantity;
    
    public boolean hasEnoughStock(int requestedQuantity) {
        return stockQuantity >= requestedQuantity;
    }
    
    public void reduceStock(int quantity) {
        if (!hasEnoughStock(quantity)) {
            throw new InsufficientStockException("Not enough stock for product: " + productCode);
        }
        this.stockQuantity -= quantity;
    }
}