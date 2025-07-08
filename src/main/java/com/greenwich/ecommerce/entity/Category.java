package com.greenwich.ecommerce.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
@Table(name = "category")
public class Category extends AbstractEntity {

    @Column(name = "name",nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

}
