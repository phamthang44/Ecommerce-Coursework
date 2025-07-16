package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.enums.StockStatus;
import com.greenwich.ecommerce.common.enums.Unit;
import com.greenwich.ecommerce.common.util.Util;
import com.greenwich.ecommerce.dto.request.AssetRequest;
import com.greenwich.ecommerce.dto.request.ProductCategoryRequestPatchDTO;
import com.greenwich.ecommerce.dto.request.ProductRequestPostDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.ProductResponseDTO;
import com.greenwich.ecommerce.entity.Asset;
import com.greenwich.ecommerce.entity.Category;
import com.greenwich.ecommerce.entity.Product;
import com.greenwich.ecommerce.exception.BadRequestException;
import com.greenwich.ecommerce.exception.InvalidDataException;
import com.greenwich.ecommerce.exception.ResourceNotFoundException;
import com.greenwich.ecommerce.repository.ProductRepository;
import com.greenwich.ecommerce.service.CategoryService;
import com.greenwich.ecommerce.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final AssetService assetService;

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
        // Assuming unit is always "pcs" for simplicity

        return convertToResponse(product);
    }

    @Override
    @Transactional
    public ProductResponseDTO createProduct(ProductRequestPostDTO dto, List<MultipartFile> files) {

        StockStatus stockStatus = dto.getStockStatus() != null ? dto.getStockStatus() : StockStatus.IN_STOCK;
        Unit unit = dto.getUnit() != null ? dto.getUnit() : Unit.PIECE;
        Category category = categoryService.getCategoryEntityById(dto.getCategoryId());
        if (category == null) {
            log.error("Create product service : Category not found with id: {}", dto.getCategoryId());
            throw new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId());
        }

        List<AssetRequest> assets = dto.getAssets();

        if (files.size() != assets.size()) {
            throw new BadRequestException("The number of images must match the number of asset metadata");
        }

        Product product = Product.builder()
                .name(dto.getProductName())
                .description(dto.getDescription())
                .unit(unit)
                .category(category)
                .stockQuantity(dto.getQuantity())
                .stockStatus(stockStatus)
                .price(dto.getPrice())
                .assets(new ArrayList<>())
                .deleted(false)
                .build();

        for (int i = 0; i < assets.size(); i++) {
            AssetRequest assetRequest = assets.get(i);
            MultipartFile file = files.get(i);
            Asset asset = assetService.uploadAsset(assetRequest, file, product);
            product.getAssets().add(asset);
        }

        productRepository.save(product);

        return convertToResponse(product);
    }




    @Override
    public PageResponse<ProductResponseDTO> getAllProductsWithPage(int pageNo, int pageSize) {
        int page = Util.getPageNo(pageNo); // Convert to zero-based index

        if (pageNo < 0 || pageSize <= 0) {
            log.error("Invalid pagination parameters: pageNo={}, pageSize={}", pageNo, pageSize);
            throw new InvalidDataException("Page number and size must be non-negative and positive respectively");
        }


        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Product> products = productRepository.findAllByDeletedFalse(pageable);
        List<ProductResponseDTO> responses = products.stream().map(this::convertToResponse).toList();

        return PageResponse.<ProductResponseDTO>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(products.getTotalPages())
                .totalElements((int) products.getTotalElements())
                .items(responses)
                .build();
    }

    public List<ProductResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAllByDeletedFalse();
        return products.stream().map(this::convertToResponse).toList();
    }

    @Override
    public ProductResponseDTO updateProduct(Long productId, ProductRequestPostDTO dto) {
        if (dto == null) {
            log.error("Update product service: ProductRequestPostDTO is null");
            throw new InvalidDataException("Product data cannot be null");
        }

        if (productId == null || productId <= 0) {
            log.error("Invalid product id: {}", productId);
            throw new InvalidDataException("Product id must be greater than zero");
        }

        Product product = productRepository.getProductById(productId);
        if (product == null) {
            log.error("Update product service: Product not found with id: {}", productId);
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        product.setName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getQuantity());
        product.setStockStatus(dto.getStockStatus() != null ? dto.getStockStatus() : StockStatus.IN_STOCK);
        product.setUnit(dto.getUnit() != null ? dto.getUnit() : Unit.PIECE);
        Category category = categoryService.getCategoryEntityById(dto.getCategoryId());
        if (category == null) {
            log.error("Update product service: Category not found with id: {}", dto.getCategoryId());
            throw new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId());
        }
        product.setCategory(category);
        productRepository.save(product);
        log.info("Update product service: Product with id {} updated successfully", productId);

        return convertToResponse(product);
    }

    @Override
    public ProductResponseDTO updateProductCategory(Long productId, ProductCategoryRequestPatchDTO dto) {
        if (dto == null) {
            log.error("Update category of product service: ProductRequestPostDTO is null");
            throw new InvalidDataException("Product data cannot be null");
        }

        if (productId == null || productId <= 0) {
            log.error("Update category product service:Invalid product id: {}", productId);
            throw new InvalidDataException("Product id must be greater than zero");
        }

        Product product = productRepository.getProductById(productId);
        if (product == null) {
            log.error("Update category of product service: Product not found with id: {}", productId);
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        Category updatedCategory = categoryService.getCategoryEntityById(dto.getCategoryId());

        product.setCategory(updatedCategory);
        productRepository.save(product);
        log.info("Update product category service: Product with id {} updated successfully", productId);

        return convertToResponse(product);
    }

    @Override
    public ProductResponseDTO createProduct(ProductRequestPostDTO dto) {
        StockStatus stockStatus = dto.getStockStatus() != null ? dto.getStockStatus() : StockStatus.IN_STOCK;
        Unit unit = dto.getUnit() != null ? dto.getUnit() : Unit.PIECE;
        Category category = categoryService.getCategoryEntityById(dto.getCategoryId());
        if (category == null) {
            log.error("Create product : Category not found with id: {}", dto.getCategoryId());
            throw new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId());
        }

        Product product = Product.builder()
                .name(dto.getProductName())
                .description(dto.getDescription())
                .unit(unit)
                .category(category)
                .stockQuantity(dto.getQuantity())
                .stockStatus(stockStatus)
                .price(dto.getPrice())
                .assets(new ArrayList<>())
                .deleted(false)
                .build();

        productRepository.save(product);

        return convertToResponse(product);

    }

    private ProductResponseDTO convertToResponse(Product product) {

        List<Asset> assets = product.getAssets() != null ? product.getAssets() : new ArrayList<>();
        Asset defaultAsset = assetService.getAssetById(2L);
        assets.add(defaultAsset);

        return ProductResponseDTO.builder()
                .id(product.getId())
                .productName(product.getName())
                .productDescription(product.getDescription())
                .productType(product.getCategory() != null ? product.getCategory().getName() : "Unknown")
                .price(product.getPrice())
                .stockStatus(product.getStockStatus())
                .quantity(product.getStockQuantity())
                .unit(product.getUnit())
                .assets(assetService.convertToListAssetResponse(assets))
                .build();
    }

    @Override
    public ProductResponseDTO updateProductAsset(Long productId, MultipartFile file) {

        Product product = productRepository.getProductById(productId);
        if (product == null) {
            log.error("Update product asset service: Product not found with id: {}", productId);
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        if (file == null || file.isEmpty()) {
            log.error("Update product asset service: File is null or empty");
            throw new InvalidDataException("File cannot be null or empty");
        }
        if (product.getAssets().isEmpty()) {
            Asset asset = assetService.uploadAsset(new AssetRequest("image", "thumbnail", true, product.getName()), file, product);
            product.getAssets().add(asset);
            productRepository.save(product);
        } else {
            List<Asset> assets = product.getAssets(); // Assuming we update the first asset
            for (int i = 0; i < assets.size(); i++) {
                Asset asset = assets.get(i);
                //kiểm tra xem asset có bị trùng ko ? check url hoặc check name để sau khi triển khai admin!
                if (asset.getProduct().getId().equals(productId)) {
                    // Update the existing asset with the new file
                    Asset updatedAsset = assetService.uploadAsset(new AssetRequest("image", "thumbnail", true, product.getName()), file, product);
                    updatedAsset.setId(asset.getId()); // Preserve the existing asset ID
                    product.getAssets().set(i, updatedAsset); // Replace the old asset with the updated one
                    productRepository.save(product);
                    return convertToResponse(product);
                }
            }
        }

        log.info("Update product asset service: Product with id {} updated successfully", productId);
        return convertToResponse(product);
    }

    @Override
    public Product getProductEntityById(Long productId) {
        if (productId == null || productId <= 0) {
            log.error("Get product entity by id service: Invalid product id: {}", productId);
            throw new InvalidDataException("Product id must be greater than zero");
        }

        Product product = productRepository.getProductById(productId);
        if (product == null) {
            log.error("Get product entity by id service: Product not found with id: {}", productId);
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        return product;
    }
}
