package com.greenwich.ecommerce.controller;

import com.greenwich.ecommerce.dto.request.CategoryRequestDTO;
import com.greenwich.ecommerce.dto.response.CategoryResponseDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.ResponseData;
import com.greenwich.ecommerce.entity.Category;
import com.greenwich.ecommerce.service.CategoryService;
import com.greenwich.ecommerce.service.impl.CategoryServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
@Validated
@Tag(name = "Category Management", description = "Endpoints for managing categories (ADMIN ROLE). Do not use this API for CUSTOMER ROLE so the Frontend will not need to implement this API")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponseDTO>> getAllCategories(@RequestParam(defaultValue = "0", required = false) int pageNo,
                                                                              @RequestParam(defaultValue = "10", required = false) int pageSize
                                                                   ) {
        log.info("Fetching all categories with page number: {} and page size: {}", pageNo, pageSize);
        return ResponseEntity.status(HttpStatus.OK.value()).body(categoryService.getAllCategories(pageNo, pageSize));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@RequestBody CategoryRequestDTO category) {
        log.info("Creating new category: {}", category);

        CategoryResponseDTO createdCategory = categoryService.createCategory(category);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(createdCategory);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable("id") Long id) {
        log.info("Fetching category with id: {}", id);
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.status(HttpStatus.OK.value()).body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable("id") @Min(1) Long id,
                                                              @RequestBody CategoryRequestDTO category) {
        log.info("Updating category with id: {}", id);
        CategoryResponseDTO updatedCategory = categoryService.updateCategory(id, category);
        return ResponseEntity.status(HttpStatus.OK.value()).body(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseData<?>> deleteCategory(@PathVariable("id") @Min(1) Long id) {
        log.info("Deleting category with id: {}", id);
        categoryService.deleteCategory(id);

        return ResponseEntity.status(200).body(new ResponseData<>(HttpStatus.OK.value(), "Category deleted successfully"));
    }

}
