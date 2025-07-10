package com.greenwich.ecommerce.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDTO {
//    private Long cartItemId;
//    private Long productId;

    private String name;

    private int quantity;

}
