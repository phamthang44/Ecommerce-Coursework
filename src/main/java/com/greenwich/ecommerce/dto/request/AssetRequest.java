package com.greenwich.ecommerce.dto.request;



import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class AssetRequest {

    private String type; //default: image!

    private String usage; // optional: thumbnail, zoom, etc.

    private boolean isPrimary; //nghĩa là ảnh chính của sản phẩm

    private String altText; // optional: mô tả ảnh, dùng cho SEO hoặc accessibility

}
