package com.greenwich.ecommerce.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductCategoryRequestPatchDTO {

    @NotNull(message = "Category ID is required")
    @Schema(
            description = "ID of the category to which the product will be assigned",
            example = "1"
    )
    private Long categoryId;

}
