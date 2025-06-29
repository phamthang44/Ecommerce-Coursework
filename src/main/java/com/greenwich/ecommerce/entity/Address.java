package com.greenwich.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "address")
public class Address extends AbstractEntity {

    @Column(name = "apartment_number", nullable = true, length = 50)
    private String apartmentNumber;

    @Column(name = "floor", nullable = true, length = 50)
    private String floor;

    @Column(name = "building", nullable = true, length = 50)
    private String building;

    @Column(name = "street_number", nullable = false, length = 50)
    private String streetNumber;

    @Column(name = "street", nullable = false, length = 50)
    private String street;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "ward", nullable = true, length = 50)
    private String ward;

    @Column(name = "district", nullable = false, length = 50)
    private String district;

    @Column(name = "country", nullable = false)
    private String country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "address_type")
    private Integer addressType;
}
