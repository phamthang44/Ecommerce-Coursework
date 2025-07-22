package com.greenwich.ecommerce.entity;

import com.greenwich.ecommerce.common.enums.PaymentStatusType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payment_status")
@AttributeOverride(name = "id", column = @Column(name = "status_id"))
public class PaymentStatus extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    private PaymentStatusType statusName;

}
