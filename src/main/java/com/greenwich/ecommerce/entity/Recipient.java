package com.greenwich.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "recipient")
@AttributeOverride(name = "id", column = @Column(name = "recipient_id"))
public class Recipient extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(name = "address_line", nullable = false)
    private String address;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "postcode", nullable = false, length = 100)
    private String postCode;

    @Column(name = "country", nullable = false, length = 50)
    private String country;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault; // true: la dia chi mac dinh, false: khong phai dia chi mac dinh

}
