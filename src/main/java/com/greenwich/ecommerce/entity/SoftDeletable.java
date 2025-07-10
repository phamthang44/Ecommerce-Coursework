package com.greenwich.ecommerce.entity;

public interface SoftDeletable {

    boolean isDeleted();
    void setDeleted(boolean deleted);

}
