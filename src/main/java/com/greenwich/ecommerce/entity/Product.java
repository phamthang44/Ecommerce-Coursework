package com.greenwich.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product extends AbstractEntity {

    @NotBlank
    @Column(name = "name",nullable = false, length = 100)
    private String name;

    @NotBlank
    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @NotNull
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull
    private Category category;


}
