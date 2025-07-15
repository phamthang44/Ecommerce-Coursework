package com.greenwich.ecommerce.dto.response;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class AssetResponse {
    private Long id;               // ID của ảnh
    private String url;           // URL ảnh đã upload
    private String usage;         // thumbnail, main_view, avatar...
    private boolean isPrimary;    // Có phải ảnh chính không
    private String altText;       // Text hiển thị khi ảnh lỗi
}
