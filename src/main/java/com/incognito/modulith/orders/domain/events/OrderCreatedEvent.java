package com.incognito.modulith.orders.domain.events;

public record OrderCreatedEvent(
        String orderNumber,
        String productCode,
        int quantity) {
}
