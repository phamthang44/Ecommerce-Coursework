package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.ProductCategoryRequestPatchDTO;
import com.greenwich.ecommerce.dto.request.ProductRequestPostDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.ProductResponseDTO;
import com.greenwich.ecommerce.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductResponseDTO getProductById(Long productId);
    ProductResponseDTO createProduct(ProductRequestPostDTO dto, List<MultipartFile> files);
    ProductResponseDTO createProduct(ProductRequestPostDTO dto);
    PageResponse<ProductResponseDTO> getAllProductsWithPage(int pageNo, int pageSize);

    List<ProductResponseDTO> getAllProducts();
    ProductResponseDTO updateProduct(Long productId, ProductRequestPostDTO dto);

    ProductResponseDTO updateProductCategory(Long productId, ProductCategoryRequestPatchDTO dto);
    ProductResponseDTO updateProductAsset(Long productId, MultipartFile file);
    Product getProductEntityById(Long productId);

}
