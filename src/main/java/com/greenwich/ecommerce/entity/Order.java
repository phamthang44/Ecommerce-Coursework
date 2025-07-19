package com.greenwich.ecommerce.entity;

import com.greenwich.ecommerce.common.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "`order`")
@AttributeOverride(name = "id", column = @Column(name = "order_id"))
public class Order extends AbstractEntity implements SoftDeletable {

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    // List of the product in the order
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "order_status_id", nullable = true)
    private Long orderStatus;

    @ManyToOne
    @JoinColumn(name = "order_channel_id", nullable = true)
    private OrderChannel orderChannel;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "discount_applied", nullable = true)
    private BigDecimal discountApplied;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

}
