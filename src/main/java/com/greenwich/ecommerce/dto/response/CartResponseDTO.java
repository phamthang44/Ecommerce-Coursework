package com.greenwich.ecommerce.dto.response;


import com.greenwich.ecommerce.entity.CartItem;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDTO implements Serializable {

    private List<CartItemResponseDTO> cartItems;
    private BigDecimal totalPrice;

}
