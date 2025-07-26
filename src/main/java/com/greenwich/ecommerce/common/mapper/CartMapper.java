package com.greenwich.ecommerce.common.mapper;

import com.greenwich.ecommerce.dto.request.CartItemRequestDTO;
import com.greenwich.ecommerce.entity.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    Cart toCart(CartItemRequestDTO dto);
}
