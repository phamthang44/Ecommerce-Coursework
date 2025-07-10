package com.greenwich.ecommerce.dto.request;

import com.greenwich.ecommerce.entity.CartItem;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CartItemRequestDTO implements Serializable {

    private Long productId;

    private Long cartId;

//    private BigInteger itemType;

    // 0 la xoa, mac dinh la 1
    private Integer quantity;
}
