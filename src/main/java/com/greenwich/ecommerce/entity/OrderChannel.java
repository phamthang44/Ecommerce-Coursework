package com.greenwich.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_channel")
@AttributeOverride(name = "id", column = @Column(name = "channel_id"))
public class OrderChannel extends AbstractEntity {
    @Column(name = "name", nullable = false)
    private String channelName;
}
