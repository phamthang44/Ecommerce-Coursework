package com.greenwich.ecommerce.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserType {
    @JsonProperty("admin")
    ADMIN,
    @JsonProperty("customer")
    CUSTOMER,
    @JsonProperty("staff")
    STAFF,
    @JsonProperty("guest")
    GUEST
}
