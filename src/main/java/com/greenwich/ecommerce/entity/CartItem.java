package com.greenwich.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "cart_item")
@AttributeOverride(name = "id", column = @Column(name = "cart_item_id"))
public class CartItem extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

//    @Column(name = "item_type", nullable = false)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_type")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

}
