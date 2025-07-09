package com.greenwich.ecommerce.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`order`")
@AttributeOverride(name = "id", column = @Column(name = "order_id"))
public class Order extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    // List of the product in the order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @ManyToOne
    @JoinColumn(name = "order_status_id", nullable = false)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "order_channel_id", nullable = false)
    private OrderChannel orderChannel;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "total_price", nullable = false)
    private Double totalPrice;

    @Column(name = "discount_applied", nullable = true)
    private double discountApplied;

}
