package com.incognito.modulith.orders.domain.models;

import jakarta.validation.Valid;

public record CreateOrderRequest(
        @Valid OrderItem item,
        @Valid Customer customer) {}
