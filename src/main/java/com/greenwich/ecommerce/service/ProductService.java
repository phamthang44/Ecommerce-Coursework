package com.greenwich.ecommerce.service;

import com.greenwich.ecommerce.dto.request.ProductRequestPostDTO;
import com.greenwich.ecommerce.dto.response.PageResponse;
import com.greenwich.ecommerce.dto.response.ProductResponseDTO;

public interface ProductService {

    ProductResponseDTO getProductById(Long productId);
    ProductResponseDTO createProduct(ProductRequestPostDTO dto);

    PageResponse<?> getAllProducts(int pageNo, int pageSize);

}
