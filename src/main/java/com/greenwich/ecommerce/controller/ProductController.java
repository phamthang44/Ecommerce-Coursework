package com.greenwich.ecommerce.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenwich.ecommerce.dto.request.ProductStockUpdatePatchDTO;
import com.greenwich.ecommerce.dto.request.ProductRequestPostDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.ProductResponseDTO;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
@Tag(name = "Product Management", description = "Endpoints for managing products (ADMIN ROLE and CUSTOMER ROLE). CRUD Products will be ADMIN ROLE, but get product by id and get all products will be CUSTOMER ROLE)")
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @GetMapping("/{id}")
    @Operation(method= "GET", summary="Get a product", description="This API endpoint allows you to view detail product.")
    public ResponseEntity<ResponseData<ProductResponseDTO>> getProductById(@Min(1) @PathVariable Long id) {
        // Logic to fetch product by ID
        log.info("Fetching product with ID: {}", id);

        // Assuming a service method fetches the product
        ProductResponseDTO product = productService.getProductById(id);

        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(HttpStatus.OK.value(), "Product found!", product));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            method= "POST",
            summary="Create a product",
            description="This API endpoint allows you to create a new product. (ADMIN)"

    )
    public ResponseEntity<ResponseData<ProductResponseDTO>> addProduct(
            @Parameter(
                    description = "Product details in JSON",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ProductRequestPostDTO.class)
                    )
            )
            @Valid @RequestPart("product") String productJson,
            @Parameter(
                    description = "List of product images",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart("images") List<MultipartFile> images) throws JsonProcessingException {

        ProductRequestPostDTO product = objectMapper.readValue(productJson, ProductRequestPostDTO.class);

        // Logic to add a new product
        log.info("Adding new product: {}", product.toString());

        // Assuming a service method adds the product
        ProductResponseDTO addedProduct = productService.createProduct(product, images);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new ResponseData<>(201, "Product added successfully!", addedProduct));
    }

//    @GetMapping
//    @Operation(method= "GET", summary="Get list of product with page", description="This API endpoint allows you to view a paginated list of products.")
//    public ResponseEntity<ResponseData<PageResponse<ProductResponseDTO>>> getAllProductsWithPage(
//        @RequestParam(defaultValue = "1", required = false) int pageNo,
//        @RequestParam(defaultValue = "10", required = false) int pageSize
//    ) {
//        log.info("Get products in controller : Request get all products with pageNo: {}, pageSize: {}", pageNo, pageSize);
//
//        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(HttpStatus.OK.value(), "Product list", productService.getAllProductsWithPage(pageNo, pageSize)));
//    }
    @GetMapping
    @Operation(method= "GET", summary="Get all or search products", description="This API endpoint returns a paginated list of products. If 'keyword' is provided, it returns filtered results.")
    public ResponseEntity<ResponseData<PageResponse<ProductResponseDTO>>> getAllOrSearchProducts(
            @RequestParam(defaultValue = "1") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword
    ) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            log.info("Search products with keyword: {}", keyword);
            var result = productService.searchProductWithKeyWord(pageNo, pageSize, keyword);
            return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Search results for keyword: " + keyword, result));
        } else {
            log.info("Get all products with pageNo: {}, pageSize: {}", pageNo, pageSize);
            var result = productService.getAllProductsWithPage(pageNo, pageSize);
            return ResponseEntity.ok(new ResponseData<>(HttpStatus.OK.value(), "Product list", result));
        }
    }

//    @GetMapping("/all")
//    @Operation(method= "GET", summary="Get list of products", description="This API endpoint allows you to view all products!! This API is for testing purposes only, not for production use. This API will get all information in database to client")
//    public ResponseEntity<ResponseData<List<ProductResponseDTO>>> getAllProducts() {
//        log.info("Get products in controller : Request get all products without pagination");
//
//        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(HttpStatus.OK.value(), "Product list", productService.getAllProducts()));
//    }

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

    @PatchMapping(value = "/{id}/asset", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method= "PATCH", summary="Update a product's asset", description="This API endpoint allows you to update a product's asset by ID. (ADMIN)")
    public ResponseEntity<ResponseData<ProductResponseDTO>> updateProductAsset(
            @PathVariable("id") @Min(1) Long id,
            @Parameter(
                    description = "Image product asset",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart("images") MultipartFile image
    )  {
        log.info("Updating product asset in controller: with ID: {}", id);
        ProductResponseDTO updatedProduct = productService.updateProductAssetPrimary(id, image);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(HttpStatus.OK.value(), "Product asset updated successfully", updatedProduct));
    }

    @PatchMapping(value = "/{id}/assets", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(method= "PATCH", summary="Update a product's assets", description="This API endpoint allows you to update a product's assets by ID. (ADMIN)")
    public ResponseEntity<ResponseData<ProductResponseDTO>> updateProductAssets(
            @PathVariable("id") @Min(1) Long id,
            @Parameter(
                    description = "List of product images",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            )
            @RequestPart("images") List<MultipartFile> images
    ) {
        log.info("Updating product assets in controller: with ID: {}", id);
        ProductResponseDTO updatedProduct = productService.uploadProductAssets(id, images);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(HttpStatus.OK.value(), "Product assets updated successfully", updatedProduct));
    }

    @PostMapping
    @Operation(method= "POST", summary="Create a product", description="This API endpoint allows you to create a new product. (ADMIN)")
    public ResponseEntity<ResponseData<ProductResponseDTO>> createProduct(
            @Valid @RequestBody ProductRequestPostDTO product
    ) {
        log.info("Creating product in controller: {}", product);
        ProductResponseDTO createdProduct = productService.createProduct(product, null);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new ResponseData<>(HttpStatus.CREATED.value(), "Product created successfully", createdProduct));
    }

    @PatchMapping("/{id}")
    @Operation(method= "PATCH", summary="Update a product stock status", description="This API endpoint allows you to update a product stock status by ID. (ADMIN)")
    public ResponseEntity<ResponseData<ProductResponseDTO>> updateCategoryOfProduct(
            @PathVariable("id") @Min(1) Long id,
            @Valid @RequestBody ProductStockUpdatePatchDTO dto
    ) {
        log.info("Updating category controller: product with ID: {}", id);
        ProductResponseDTO updatedProduct = productService.updateProductStockStatus(id, dto);
        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(HttpStatus.OK.value(), "Product stock status updated successfully", updatedProduct));
    }
//    @PatchMapping("/{id}")
//    @Operation(method= "PATCH", summary="Update a product category", description="This API endpoint allows you to update a product stock status by ID. (ADMIN)")
//    public ResponseEntity<ResponseData<ProductResponseDTO>> updateProductStockStatus(
//            @PathVariable("id") @Min(1) Long id,
//            @Valid @RequestBody ProductStockUpdatePatchDTO dto
//    ) {
//        log.info("Updating product stock status controller: product with ID: {}", id);
//        ProductResponseDTO updatedProduct = productService.updateProductCategory(id, dto);
//        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(HttpStatus.OK.value(), "Product type updated successfully", updatedProduct));
//    }
//    @GetMapping
//    @Operation(method= "GET", summary="Search products by keyword", description="This API endpoint allows you to search products by keyword. (CUSTOMER)")
//    public ResponseEntity<ResponseData<PageResponse<ProductResponseDTO>>> searchProductWithKeyWord(
//            @RequestParam(defaultValue = "1", required = false) int pageNo,
//            @RequestParam(defaultValue = "10", required = false) int pageSize,
//            @RequestParam String keyword
//    ) {
//        log.info("Searching products with keyword: {}", keyword);
//        PageResponse<ProductResponseDTO> response = productService.searchProductWithKeyWord(pageNo, pageSize, keyword);
//        return ResponseEntity.status(HttpStatus.OK.value()).body(new ResponseData<>(HttpStatus.OK.value(), "Search results for keyword: " + keyword, response));
//    }


}
