package com.greenwich.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@AttributeOverride(name = "id", column = @Column(name = "product_id"))
public class Product extends AbstractEntity {

    @Column(name = "name",nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Column(name = "stock_status", nullable = false)
    private String stockStatus;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


}
