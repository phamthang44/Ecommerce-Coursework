package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.common.enums.StockStatus;
import com.greenwich.ecommerce.common.enums.Unit;
import com.greenwich.ecommerce.common.util.Util;
import com.greenwich.ecommerce.dto.request.AssetRequest;
import com.greenwich.ecommerce.dto.request.ProductStockUpdatePatchDTO;
import com.greenwich.ecommerce.dto.request.ProductRequestPostDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.ProductResponseDTO;
import com.greenwich.ecommerce.entity.Asset;
import com.greenwich.ecommerce.entity.Category;
import com.greenwich.ecommerce.entity.Product;
import com.greenwich.ecommerce.exception.BadRequestException;
import com.greenwich.ecommerce.exception.InvalidDataException;
import com.greenwich.ecommerce.exception.OutOfStockException;
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

    @Transactional
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
        if (dto.getQuantity() < 0) {
            log.error("Update product service: Quantity cannot be negative");
            throw new InvalidDataException("Quantity cannot be negative");
        }
        if (dto.getProductName() == null || dto.getProductName().isEmpty()) {
            log.error("Update product service: Product name cannot be null or empty");
            throw new InvalidDataException("Product name cannot be null or empty");
        }
        if (dto.getDescription() == null || dto.getDescription().isEmpty()) {
            log.error("Update product service: Product description cannot be null or empty");
            throw new InvalidDataException("Product description cannot be null or empty");
        }

        product.setName(dto.getProductName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getQuantity());
        product.setStockStatus(autoUpdateStatus(product));
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

    private StockStatus autoUpdateStatus(Product product) {
        if (product.getStockQuantity() <= 0) {
            product.setStockStatus(StockStatus.OUT_OF_STOCK);
        } else {
            product.setStockStatus(StockStatus.IN_STOCK);
        }
        return product.getStockStatus();
    }

    @Transactional
    @Override
    public ProductResponseDTO updateProductStockStatus(Long productId, ProductStockUpdatePatchDTO dto) {
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
        if (dto.getQuantity() < 0) {
            log.error("Update product category service: Quantity cannot be negative");
            throw new InvalidDataException("Quantity cannot be negative");
        }
        product.setStockQuantity(dto.getQuantity());
        if (product.getStockQuantity() == 0) {
            log.warn("Update product stock status service: Quantity is zero, setting stock status to OUT_OF_STOCK");
            product.setStockStatus(StockStatus.OUT_OF_STOCK);
        } else if (product.getStockQuantity() > 0) {
            log.info("Update product stock status service: Quantity is greater than zero, setting stock status to IN_STOCK");
            product.setStockStatus(StockStatus.IN_STOCK);
        } else {
            log.error("Update product stock status service: Invalid quantity: {}", dto.getQuantity());
            throw new InvalidDataException("Invalid quantity: " + dto.getQuantity());
        }
        log.info("Update product stock status service: Product with id {} updated successfully", productId);
        return convertToResponse(productRepository.save(product));
    }

    @Transactional
    @Override
    public ProductResponseDTO createProduct(ProductRequestPostDTO dto) {

        int quantity = dto.getQuantity() != null ? dto.getQuantity() : 0;
        if (quantity < 0) {
            log.error("Create product : Quantity cannot be negative");
            throw new InvalidDataException("Quantity cannot be negative");
        }
        if (dto.getProductName() == null || dto.getProductName().isEmpty()) {
            log.error("Create product : Product name cannot be null or empty");
            throw new InvalidDataException("Product name cannot be null or empty");
        }
        if (dto.getDescription() == null || dto.getDescription().isEmpty()) {
            log.error("Create product : Product description cannot be null or empty");
            throw new InvalidDataException("Product description cannot be null or empty");
        }
        if (dto.getPrice() == null) {
            log.error("Create product : Price cannot be null or negative");
            throw new InvalidDataException("Price cannot be null or negative");
        }
        if (dto.getCategoryId() == null || dto.getCategoryId() <= 0) {
            log.error("Create product : Category ID must be greater than zero");
            throw new InvalidDataException("Category ID must be greater than zero");
        }
        if (dto.getAssets() == null || dto.getAssets().isEmpty()) {
            log.error("Create product : Assets cannot be null or empty");
            throw new InvalidDataException("Assets cannot be null or empty");
        }
        StockStatus stockStatus = null;
        if (quantity == 0) {
            log.warn("Create product : Quantity is zero, setting stock status to OUT_OF_STOCK");
            stockStatus = StockStatus.OUT_OF_STOCK;
        } else {
            stockStatus = StockStatus.IN_STOCK;
        }

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
//        Asset defaultAsset = assetService.getAssetById(2L);
//        assets.add(defaultAsset);

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

    @Transactional
    @Override
    public ProductResponseDTO updateProductAssetPrimary(Long productId, MultipartFile file) {
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
                if (asset.isPrimary()) {
                    asset.setPrimary(false);
                    // Update the existing primary asset with the new file
                    Asset updatedAsset = assetService.uploadAsset(new AssetRequest("image", "thumbnail", true, product.getName()), file, product);
                    product.getAssets().set(i, updatedAsset); // Replace the old asset with the updated one
                    updatedAsset.setPrimary(true);
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

    @Override
    public ProductResponseDTO deleteProduct(Long productId) {
        return null;
    }

    @Transactional
    @Override
    public ProductResponseDTO uploadProductAssets(Long productId, List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            log.error("Update product assets service: Files cannot be null or empty");
            throw new InvalidDataException("Files cannot be null or empty");
        }
        if (productId == null || productId <= 0) {
            log.error("Update product assets service: Invalid product id: {}", productId);
            throw new InvalidDataException("Product id must be greater than zero");
        }
        Product product = productRepository.getProductById(productId);
        if (product == null) {
            log.error("Update product assets service: Product not found with id: {}", productId);
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        List<Asset> assets = product.getAssets() != null ? product.getAssets() : new ArrayList<>();
        for (MultipartFile file : files) {
            Asset asset = assetService.uploadAsset(new AssetRequest("image", "thumbnail", false, product.getName()), file, product);
            assets.add(asset);
        }
        product.setAssets(assets);
        productRepository.save(product);
        return convertToResponse(product);
    }

    @Override
    public PageResponse<ProductResponseDTO> searchProductWithKeyWord(int pageNo, int pageSize, String keyword) {
        int page = 0;
        if (pageNo > 0) {
            page = pageNo - 1; // Convert to zero-based index
        }
        if (pageNo < 0 || pageSize <= 0) {
            log.error("Search product: Invalid pagination parameters: pageNo={}, pageSize={}", pageNo, pageSize);
            throw new InvalidDataException("Page number and size must be non-negative and positive respectively");
        }
        if (keyword == null || keyword.isBlank()) {
            log.error("Search product: Keyword cannot be null or empty");
            throw new InvalidDataException("Keyword cannot be null or empty");
        }

        keyword = keyword.trim();

        Pageable pageable = PageRequest.of(page, pageSize);

        List<Product> products = productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(keyword, pageable);
        System.out.println("Search keyword: " + products.toString());
        if (products.isEmpty()) {
            log.warn("No products found with keyword: {}", keyword);
            return PageResponse.<ProductResponseDTO>builder()
                    .pageNo(pageNo)
                    .pageSize(pageSize)
                    .totalPages(0)
                    .totalElements(0)
                    .items(new ArrayList<>())
                    .build();
        }
        List<ProductResponseDTO> responses = products.stream()
                .map(this::convertToResponse)
                .toList();


        return PageResponse.<ProductResponseDTO>builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages((int) Math.ceil((double) products.size() / pageSize))
                .totalElements(products.size())
                .items(responses)
                .build();
    }

    @Override
    public ProductResponseDTO updateProductAsset(Long productId, MultipartFile file, boolean isPrimary) {
        return null;
    }

    @Override
    public ProductResponseDTO updateProductQuantity(Long productId, int quantity) {

        if (productId == null || productId <= 0) {
                log.error("Update product quantity service: Invalid product id: {}", productId);
                throw new InvalidDataException("Product id must be greater than zero");
        }

        Product product = productRepository.getProductById(productId);
        if (product == null) {
            log.error("Update product quantity service: Product not found with id: {}", productId);
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        if (quantity < 0) {
            log.error("Update product quantity service: Quantity cannot be negative");
            throw new InvalidDataException("Quantity cannot be negative");
        }
        product.setStockQuantity(quantity);
        return convertToResponse(productRepository.save(product));
    }

    @Override
    public void decreaseProductQuantity(Long productId, int quantity) {
        if (productId == null || productId <= 0) {
            log.error("Decrease product quantity service: Invalid product id: {}", productId);
            throw new InvalidDataException("Product id must be greater than zero");
        }

        Product product = productRepository.getProductById(productId);
        if (product == null) {
            log.error("Decrease product quantity service: Product not found with id: {}", productId);
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }

        if (quantity < 0) {
            log.error("Decrease product quantity service: Quantity cannot be negative");
            throw new InvalidDataException("Quantity cannot be negative");
        }

        if (product.getStockQuantity() < quantity) {
            log.warn("Decrease product quantity service: Insufficient stock for product id: {}", productId);
            throw new OutOfStockException("Product is out of stock or insufficient stock");
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        Product updatedProduct = productRepository.save(product);
        log.info("Stock decreased successfully for product id: {}, new stock: {}", productId, product.getStockQuantity());

        if (updatedProduct.getStockQuantity() == 0) {
            updatedProduct.setStockStatus(StockStatus.OUT_OF_STOCK);
            productRepository.save(updatedProduct);
            log.info("Product id: {} is now out of stock", productId);
        } else {
            updatedProduct.setStockStatus(StockStatus.IN_STOCK);
            productRepository.save(updatedProduct);
            log.info("Product id: {} is now in stock", productId);
        }

    }

    @Override
    public void increaseStock(Long productId, int quantity) {

    }
}
