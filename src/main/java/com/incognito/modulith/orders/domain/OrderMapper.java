package com.incognito.modulith.orders.domain;


import com.incognito.modulith.orders.domain.models.CreateOrderRequest;
import com.incognito.modulith.orders.domain.models.OrderDTO;
import com.incognito.modulith.orders.domain.models.OrderStatus;

import java.util.UUID;

class OrderMapper {

    static OrderEntity convertToEntity(CreateOrderRequest request) {
        OrderEntity entity = new OrderEntity();
        entity.setOrderNumber(UUID.randomUUID().toString());
        entity.setStatus(OrderStatus.NEW);
        entity.setCustomer(request.customer());
        entity.setOrderItem(request.item());
        return entity;
    }

    static OrderDTO convertToDTO(OrderEntity order) {
        return new OrderDTO(
                order.getOrderNumber(),
                order.getOrderItem(),
                order.getCustomer(),
                order.getStatus(),
                order.getCreatedAt());
    }
}
