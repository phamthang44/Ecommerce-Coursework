package com.greenwich.ecommerce.dto.response;


import com.greenwich.ecommerce.entity.CartItem;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDTO {

    private Long userId;

    private BigDecimal totalPrice;
    private List<CartItem> cartItems = new ArrayList<>();
}
