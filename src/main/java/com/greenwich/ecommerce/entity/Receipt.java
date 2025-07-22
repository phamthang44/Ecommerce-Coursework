package com.greenwich.ecommerce.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@Table(name = "receipt")
@AllArgsConstructor
@AttributeOverride(name="id", column = @Column(name = "receipt_id"))
public class Receipt extends AbstractEntity implements SoftDeletable{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name="is_deleted", nullable = false)
    private boolean deleted = false;

    @Override
    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
