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

    private List<CartItemResponseDTO> cartItems;
    private BigDecimal totalPrice;

}
