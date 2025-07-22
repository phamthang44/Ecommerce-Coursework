package com.greenwich.ecommerce.entity;

import com.greenwich.ecommerce.common.enums.OrderStatusType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_status")
@AttributeOverride(name = "id", column = @Column(name = "status_id"))
public class OrderStatus extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private OrderStatusType statusName;

}
