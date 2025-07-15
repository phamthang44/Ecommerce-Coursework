package com.greenwich.ecommerce.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "asset")
@AttributeOverride(name = "id", column = @Column(name = "asset_id"))
public class Asset extends AbstractEntity {
    @Column(name = "file_url", nullable = false)
    private String url;

    @Column(name = "file_type", nullable = false)
    private String type;

    @Column(name = "`usage`")
    private String usage;

    @ManyToOne
    @JoinColumn(name = "usage_id")
    @JsonBackReference
    private Product product;

    @Column(name = "is_primary", nullable = false)
    private boolean isPrimary;

    @Column(name = "alt_text")
    private String altText;

    // Uploaded_at trung voi Created_at nen xoa, chi giu lai 1 cai
}
