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
    GUEST;

    public static UserType fromString(String type) {
        for (UserType userType : UserType.values()) {
            if (userType.name().equalsIgnoreCase(type)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Unknown user type: " + type);
    }

    public String getName() {
        return this.name();
    }
}
