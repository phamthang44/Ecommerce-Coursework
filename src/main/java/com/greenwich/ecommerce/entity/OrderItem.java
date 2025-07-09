package com.greenwich.ecommerce.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_item")
@AttributeOverride(name = "id", column = @Column(name = "order_item_id"))
public class OrderItem extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "item_id", nullable = false)
    private Long itemId; // Cai nay la product_id nhung ma hardcode

    @Column(name = "item_type", nullable = false)
    private String itemType;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    private Double price;

    @Column(name = "subtotal", nullable = true)
    private Double subtotal;

}
