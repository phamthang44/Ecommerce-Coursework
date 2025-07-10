package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.ProductRequestPostDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.ProductResponseDTO;

import java.util.List;

public interface ProductService {

    ProductResponseDTO getProductById(Long productId);
    ProductResponseDTO createProduct(ProductRequestPostDTO dto);

    PageResponse<ProductResponseDTO> getAllProductsWithPage(int pageNo, int pageSize);

    List<ProductResponseDTO> getAllProducts();

}
