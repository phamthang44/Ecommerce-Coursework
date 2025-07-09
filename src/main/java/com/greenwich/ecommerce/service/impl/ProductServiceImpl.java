package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.dto.request.ProductRequestPostDTO;
import com.greenwich.ecommerce.dto.response.ProductResponseDTO;
import com.greenwich.ecommerce.entity.Product;
import com.greenwich.ecommerce.exception.InvalidDataException;
import com.greenwich.ecommerce.exception.ResourceNotFoundException;
import com.greenwich.ecommerce.repository.ProductRepository;
import com.greenwich.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    public ProductResponseDTO getProductById(Long productId) {

        if (productId == null) {
            log.error("Product id is null");
           throw new InvalidDataException("Product id is null");
        }
        if (productId <= 0) {
            log.error("Product id is negative");
            throw new InvalidParameterException("Product id must be greater than zero");
        }

        Product product = productRepository.getProductById(productId);
        if (product == null) {
            log.error("Product not found with id: {}", productId);
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        // Assuming stock status is always in stock for simplicity
        // Assuming unit is always "pcs" for simplicity)

        return ProductResponseDTO.builder()
                .productName(product.getName())
                .productDescription(product.getDescription())
                .price(product.getPrice())
                .stockStatus("In Stock") // Assuming stock status is always in stock for simplicity
                .quantity(100)
                .unit("pcs") // Assuming unit is always "pcs" for simplicity)
                .build();
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestPostDTO dto) {

        Product product = Product.builder()
                .name(dto.getProductName())
                .description(dto.getDescription())
                .unit(dto.getUnit())
                .stockQuantity(dto.getQuantity())
                .stockStatus("In Stock")
                .price(dto.getPrice())
                .build();

        productRepository.save(product);

        return ProductResponseDTO.builder()
                .productName(product.getName())
                .productDescription(product.getDescription())
                .price(product.getPrice())
                .stockStatus(product.getStockStatus())
                .quantity(product.getStockQuantity())
                .unit(product.getUnit())
                .build();
    }
}
