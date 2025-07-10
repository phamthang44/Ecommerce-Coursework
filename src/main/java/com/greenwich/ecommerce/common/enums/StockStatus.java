package com.greenwich.ecommerce.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum StockStatus {
    @JsonProperty("in_stock")
    IN_STOCK,
    @JsonProperty("out_of_stock")
    OUT_OF_STOCK,
    @JsonProperty("limited")
    LIMITED,
    @JsonProperty("preorder")
    PREORDER
}
