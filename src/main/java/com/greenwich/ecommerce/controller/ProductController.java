package com.greenwich.ecommerce.controller;


import com.greenwich.ecommerce.dto.request.ProductRequestPostDTO;
import com.greenwich.ecommerce.dto.response.ProductResponseDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseData<ProductResponseDTO>> getProductById(@PathVariable Long id) {
        // Logic to fetch product by ID
        log.info("Fetching product with ID: {}", id);

        // Assuming a service method fetches the product
        ProductResponseDTO product = productService.getProductById(id);

        return ResponseEntity.status(200).body(new ResponseData<>(200, "Product found!", product));
    }

    @PostMapping("/")
    public ResponseEntity<ResponseData<ProductResponseDTO>> addProduct(@RequestBody ProductRequestPostDTO product) {
        // Logic to add a new product
        log.info("Adding new product: {}", product);

        // Assuming a service method adds the product
        ProductResponseDTO addedProduct = productService.createProduct(product);

        return ResponseEntity.status(201).body(new ResponseData<>(201, "Product added successfully!", addedProduct));
    }
}
