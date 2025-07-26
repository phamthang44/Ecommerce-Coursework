package com.greenwich.ecommerce.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Unit {
    @JsonProperty("piece")
    PIECE,
    @JsonProperty("box")
    BOX,
    @JsonProperty("gb")
    GB,
    @JsonProperty("month")
    MONTH
}
