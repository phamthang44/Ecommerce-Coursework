package com.greenwich.ecommerce.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    @Column(name = "status_name", nullable = false)
    private String statusName;
}
