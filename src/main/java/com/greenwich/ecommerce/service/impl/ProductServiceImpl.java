package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.enums.StockStatus;
import com.greenwich.ecommerce.common.enums.Unit;
import com.greenwich.ecommerce.common.util.Util;
import com.greenwich.ecommerce.dto.request.ProductRequestPostDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.ProductResponseDTO;
import com.greenwich.ecommerce.entity.Product;
import com.greenwich.ecommerce.exception.InvalidDataException;
import com.greenwich.ecommerce.exception.ResourceNotFoundException;
import com.greenwich.ecommerce.repository.ProductRepository;
import com.greenwich.ecommerce.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
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
                .id(product.getId())
                .productName(product.getName())
                .productDescription(product.getDescription())
                .price(product.getPrice())
                .stockStatus(product.getStockStatus())
                .quantity(product.getStockQuantity())
                .unit(product.getUnit())
                .build();
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestPostDTO dto) {

        StockStatus stockStatus = dto.getStockStatus() != StockStatus.IN_STOCK ? dto.getStockStatus() : StockStatus.IN_STOCK;
        Unit unit = dto.getUnit() != Unit.PIECE ? dto.getUnit() : Unit.PIECE;

        Product product = Product.builder()
                .name(dto.getProductName())
                .description(dto.getDescription())
                .unit(unit)
                .stockQuantity(dto.getQuantity())
                .stockStatus(stockStatus)
                .price(dto.getPrice())
                .build();

        productRepository.save(product);

        return ProductResponseDTO.builder()
                .id(product.getId())
                .productName(product.getName())
                .productDescription(product.getDescription())
                .price(product.getPrice())
                .stockStatus(product.getStockStatus())
                .quantity(product.getStockQuantity())
                .unit(product.getUnit())
                .build();
    }


    @Override
    public PageResponse<ProductResponseDTO> getAllProducts(int pageNo, int pageSize) {
        int page = Util.getPageNo(pageNo); // Convert to zero-based index

        if (pageNo < 0 || pageSize <= 0) {
            log.error("Invalid pagination parameters: pageNo={}, pageSize={}", pageNo, pageSize);
            throw new InvalidDataException("Page number and size must be non-negative and positive respectively");
        }


        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> products = productRepository.findAllByDeletedFalse(pageable);
        List<ProductResponseDTO> responses = products.stream().map(product -> ProductResponseDTO.builder()
                .id(product.getId())
                .productName(product.getName())
                .productDescription(product.getDescription())
                .price(product.getPrice())
                .stockStatus(product.getStockStatus())
                .quantity(product.getStockQuantity())
                .unit(product.getUnit())
                .build()).toList();

        return PageResponse.<ProductResponseDTO>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(products.getTotalPages())
                .totalElements((int) products.getTotalElements())
                .items(responses)
                .build();
    }
}
