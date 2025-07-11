package com.greenwich.ecommerce.controller;


import com.greenwich.ecommerce.dto.request.ProductCategoryRequestPatchDTO;
import com.greenwich.ecommerce.dto.request.ProductRequestPostDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.ProductResponseDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.service.ProductService;
import com.greenwich.ecommerce.service.impl.ProductServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
@Tag(name = "Product Management", description = "Endpoints for managing products (ADMIN ROLE and CUSTOMER ROLE). CRUD Products will be ADMIN ROLE, but get product by id and get all products will be CUSTOMER ROLE)")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    @Operation(method= "GET", summary="Get a product", description="This API endpoint allows you to view detail product.")
    public ResponseEntity<ResponseData<ProductResponseDTO>> getProductById(@Min(1) @PathVariable Long id) {
        // Logic to fetch product by ID
        log.info("Fetching product with ID: {}", id);

        // Assuming a service method fetches the product
        ProductResponseDTO product = productService.getProductById(id);

        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(HttpStatus.OK.value(), "Product found!", product));
    }

    @PostMapping
    @Operation(method= "POST", summary="Create a product", description="This API endpoint allows you to create a new product. (ADMIN)")
    public ResponseEntity<ResponseData<ProductResponseDTO>> addProduct(@Valid @RequestBody ProductRequestPostDTO product) {
        // Logic to add a new product
        log.info("Adding new product: {}", product);

        // Assuming a service method adds the product
        ProductResponseDTO addedProduct = productService.createProduct(product);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new ResponseData<>(201, "Product added successfully!", addedProduct));
    }

    @GetMapping
    @Operation(method= "GET", summary="Get list of product with page", description="This API endpoint allows you to view a paginated list of products.")
    public ResponseEntity<ResponseData<PageResponse<ProductResponseDTO>>> getAllProductsWithPage(
        @RequestParam(defaultValue = "0", required = false) int pageNo,
        @RequestParam(defaultValue = "10", required = false) int pageSize
    ) {
        log.info("Get products in controller : Request get all products with pageNo: {}, pageSize: {}", pageNo, pageSize);

        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(HttpStatus.OK.value(), "Product list", productService.getAllProductsWithPage(pageNo, pageSize)));
    }

    @GetMapping("/all")
    @Operation(method= "GET", summary="Get list of products", description="This API endpoint allows you to view all products!! This API is for testing purposes only, not for production use. This API will get all information in database to client")
    public ResponseEntity<ResponseData<List<ProductResponseDTO>>> getAllProducts() {
        log.info("Get products in controller : Request get all products without pagination");

        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(HttpStatus.OK.value(), "Product list", productService.getAllProducts()));
    }

    @PutMapping("/{id}")
    @Operation(method= "PUT", summary="Update a product", description="This API endpoint allows you to update a product by ID. (ADMIN)")
    public ResponseEntity<ResponseData<ProductResponseDTO>> updateProduct(
            @PathVariable("id") @Min(1) Long id,
            @Valid @RequestBody ProductRequestPostDTO product
    ) {
        log.info("Updating product in controller: with ID: {}", id);
        ProductResponseDTO updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(HttpStatus.OK.value(), "Product updated successfully", updatedProduct));
    }

    @PatchMapping("/{id}")
    @Operation(method= "PUT", summary="Update a product", description="This API endpoint allows you to update a product by ID. (ADMIN)")
    public ResponseEntity<ResponseData<ProductResponseDTO>> updateCategoryOfProduct(
            @PathVariable("id") @Min(1) Long id,
            @Valid @RequestBody ProductCategoryRequestPatchDTO dto
    ) {
        log.info("Updating category controller: product with ID: {}", id);
        ProductResponseDTO updatedProduct = productService.updateProductCategory(id, dto);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(HttpStatus.OK.value(), "Product type updated successfully", updatedProduct));
    }



}
