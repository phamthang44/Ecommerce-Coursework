package com.greenwich.ecommerce.common.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum UserStatus {
    @JsonProperty("active")
    ACTIVE,
    @JsonProperty("inactive")
    INACTIVE,
    @JsonProperty("isBanned")
    IS_BANNED,
    @JsonProperty("isDeleted")
    DELETED
}
