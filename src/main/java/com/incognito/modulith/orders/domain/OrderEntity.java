package com.incognito.modulith.orders.domain;


import com.incognito.modulith.orders.domain.models.Customer;
import com.incognito.modulith.orders.domain.models.OrderItem;
import com.incognito.modulith.orders.domain.models.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Setter
@Getter
class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_generator")
    @SequenceGenerator(name = "order_id_generator", sequenceName = "order_id_seq")
    private Long id;

    @Column(nullable = false)
    private String orderNumber;

    @Embedded
    @AttributeOverrides(
            value = {
                @AttributeOverride(name = "name", column = @Column(name = "customer_name")),
                @AttributeOverride(name = "email", column = @Column(name = "customer_email")),
                @AttributeOverride(name = "phone", column = @Column(name = "customer_phone"))
            })
    private Customer customer;

    @Embedded
    @AttributeOverrides(
            value = {
                    @AttributeOverride(name = "code", column = @Column(name = "product_code")),
                    @AttributeOverride(name = "name", column = @Column(name = "product_name")),
                    @AttributeOverride(name = "price", column = @Column(name = "product_price")),
                    @AttributeOverride(name = "quantity", column = @Column(name = "quantity"))
            })
    private OrderItem orderItem;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
