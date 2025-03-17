package com.incognito.modulith.orders.web.controllers;

import com.incognito.modulith.orders.domain.OrderNotFoundException;
import com.incognito.modulith.orders.domain.OrderService;
import com.incognito.modulith.orders.domain.models.CreateOrderRequest;
import com.incognito.modulith.orders.domain.models.CreateOrderResponse;
import com.incognito.modulith.orders.domain.models.OrderDTO;
import com.incognito.modulith.orders.domain.models.OrderSummary;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping
    List<OrderSummary> getOrders() {
        return orderService.findOrders();
    }

    @GetMapping(value = "/{orderNumber}")
    OrderDTO getOrder(@PathVariable(value = "orderNumber") String orderNumber) {
        log.info("Fetching order by orderNumber: {}", orderNumber);
        return orderService.findOrder(orderNumber)
                .orElseThrow(() -> OrderNotFoundException.forOrderNumber(orderNumber));
    }
}
