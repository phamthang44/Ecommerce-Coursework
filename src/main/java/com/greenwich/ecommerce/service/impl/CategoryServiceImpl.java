package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.mapper.CategoryMapper;
import com.greenwich.ecommerce.common.util.Util;
import com.greenwich.ecommerce.dto.request.CategoryRequestDTO;
import com.greenwich.ecommerce.dto.response.CategoryResponseDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.entity.Category;
import com.greenwich.ecommerce.exception.BadRequestException;
import com.greenwich.ecommerce.exception.DuplicateResourceException;
import com.greenwich.ecommerce.exception.InvalidDataException;
import com.greenwich.ecommerce.exception.ResourceNotFoundException;
import com.greenwich.ecommerce.repository.CategoryRepository;
import com.greenwich.ecommerce.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryResponseDTO createCategory(CategoryRequestDTO categoryRequestDTO) {

        if (categoryRequestDTO == null) {
            log.error("Category request is null");
            throw new BadRequestException("Category request cannot be null");
        }

        String categoryName = categoryRequestDTO.getCategoryName().trim();
        String categoryDescription = categoryRequestDTO.getCategoryDescription();

        log.info ("Creating category with name: {}", categoryName);
        log.info ("Creating category with description: {}", categoryDescription);

        categoryValidator.isValidCategoryName(categoryName);
        categoryValidator.isValidCategoryDescription(categoryDescription);

        if (categoryRepository.existsByName(categoryName)) {
            log.warn("Create Category : Category with name {} already exists", categoryRequestDTO.getCategoryName());
            throw new DuplicateResourceException("Category with this name already exists");
        }

        Category category = categoryMapper.toEntity(categoryRequestDTO);
        category = categoryRepository.save(category);
        log.info("Category created successfully with id: {}", category.getId());

        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public CategoryResponseDTO updateCategory(Long id, CategoryRequestDTO categoryRequestDTO) {

        if (categoryRequestDTO == null) {
            log.error("Update Category : Category request is null");
            throw new BadRequestException("Category request cannot be null");
        }

        String categoryName = categoryRequestDTO.getCategoryName().trim();
        String categoryDescription = categoryRequestDTO.getCategoryDescription();

        categoryValidator.isValidCategoryId(id);
        categoryValidator.isValidCategoryName(categoryName);
        categoryValidator.isValidCategoryDescription(categoryDescription);

        log.info("Updating category with id: {}", id);

        Category existingCategory = checkExistingCategoryById(id);
        Optional<Category> categoryByName = categoryRepository.findByName(categoryName);

        // Check if the category name already exists and is not the same as the current category
        // If it exists and the id is different, throw an exception
        //Đọc như sau đầu tiên kiểm sao coi findByName có trả về khác null hay ko nếu có thì true ko thì false
        // sau đó get() trả về đối tượng Category thật Optional<Category> cái bên trong <>
        // nếu có thì check id của nó xem khác id truyền vào từ request hay ko
        // nếu khác thì báo lỗi trùng tên
        // nếu ko khác thì tức là id ở trên request khớp với id vừa lôi từ db lên để kiểm tra
        // nghĩa là đang sửa object mà mình truyền vào
        if (categoryByName.isPresent() && !categoryByName.get().getId().equals(id)) {
            log.warn("Update Category : Category name '{}' already exists", categoryName);
            throw new DuplicateResourceException("Category with this name already exists");
        }

        existingCategory.setName(categoryName);
        existingCategory.setDescription(categoryDescription);

        existingCategory = categoryRepository.save(existingCategory);
        log.info("Category updated successfully with id: {}", id);

        return categoryMapper.toDto(existingCategory);

    }

    @Override
    public CategoryResponseDTO getCategoryById(Long id) {

        categoryValidator.isValidCategoryId(id);
        Category category = checkExistingCategoryById(id);

        if (category.isDeleted()) {
            log.warn("Get Category : Category with id {} is deleted", id);
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }

        log.info("Category found with id: {}", id);
        return categoryMapper.toDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {

        Category category = checkExistingCategoryById(id);
        log.info("Deleting category with id: {}", id);

        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            log.warn("Delete Category : Cannot delete category with id {} because it has associated products", id);
            throw new BadRequestException("Cannot delete category with associated products");
        }

        category.setDeleted(true);
        categoryRepository.save(category);

    }

    @Override
    public List<CategoryResponseDTO> getAllCategories() {
    List<Category> categories = categoryRepository.findAllByDeletedFalse();
        return categories.stream().map(categoryMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public PageResponse<CategoryResponseDTO> getAllCategories(int pageNo, int pageSize) {
        int page = Util.getPageNo(pageNo);

        if (pageNo < 0 || pageSize <= 0) {
            log.error("Invalid pagination parameters: pageNo={}, pageSize={}", pageNo, pageSize);
            throw new InvalidDataException("Page number and size must be non-negative and positive respectively");
        }

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Category> categories = categoryRepository.findAllByDeletedFalse(pageable);
        List<CategoryResponseDTO> responses = categories.stream().map(category -> CategoryResponseDTO.builder()
                .id(category.getId())
                .categoryName(category.getName())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build()).toList();

        return PageResponse.<CategoryResponseDTO>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(categories.getTotalPages())
                .totalElements((int) categories.getTotalElements())
                .items(responses)
                .build();
    }

    private Category checkExistingCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private boolean checkDeletedCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(Category::isDeleted)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }


}
