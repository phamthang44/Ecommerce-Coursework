package com.greenwich.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_address")
public class Address extends AbstractEntity {

    @Column(name = "address_line", nullable = false)
    private String userAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "city")
    private String city;

    @Column(name = "postcode")
    private String postalCode;

    @Column(name = "country")
    private String country;

    @Column(name = "is_default")
    private boolean isDefault;

}
