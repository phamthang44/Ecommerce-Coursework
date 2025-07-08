package com.greenwich.ecommerce.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "`order`")
@AttributeOverride(name = "id", column = @Column(name = "order_id"))
public class Order extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

//    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<OrderItem> orderItems = new ArrayList<>();


}
