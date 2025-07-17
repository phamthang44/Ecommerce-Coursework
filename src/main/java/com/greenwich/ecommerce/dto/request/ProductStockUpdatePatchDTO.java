package com.greenwich.ecommerce.dto.request;

import com.greenwich.ecommerce.common.enums.StockStatus;
import com.greenwich.ecommerce.dto.validator.EnumPattern;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ProductStockUpdatePatchDTO {

    @Min(0)
    @NotNull(message = "Quantity is required")
    @Schema(description = "Quantity of the product to update", example = "10")
    private Integer quantity;


}
