package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.CategoryRequestDTO;
import com.greenwich.ecommerce.dto.response.CategoryResponseDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.entity.Category;

import java.util.List;

public interface CategoryService {

    CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO);
    CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO);
    CategoryResponseDTO getCategoryById(Long id);
    void deleteCategory(Long id);

    List<CategoryResponseDTO> getAllCategories();
    PageResponse<CategoryResponseDTO> getAllCategories(int pageNo, int pageSize);
    Category getCategoryEntityById(Long id);
}
