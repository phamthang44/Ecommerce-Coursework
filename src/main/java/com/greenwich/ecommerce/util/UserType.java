package com.greenwich.ecommerce.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserType {
    @JsonProperty("admin")
    ADMIN,
    @JsonProperty("customer")
    CUSTOMER,
    @JsonProperty("seller")
    SELLER,
    @JsonProperty("guest")
    GUEST
}
