package com.greenwich.ecommerce.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderStatusType {
    @JsonProperty("pending")
    PENDING,
    @JsonProperty("confirmed")
    CONFIRMED,
    @JsonProperty("shipping")
    SHIPPING,
    @JsonProperty("delivered")
    DELIVERED,
    @JsonProperty("canceled")
    CANCELED,
    @JsonProperty("returned")
    RETURNED,
    @JsonProperty("failed")
    FAILED
}
