package com.greenwich.ecommerce.repository;

import com.greenwich.ecommerce.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findAllByProductId(Long productId);

    @Modifying
    @Query("DELETE FROM Asset a WHERE a.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);

    @Query("SELECT a FROM Asset a WHERE a.product.id = :usageId")
    Asset findByUsageId(@Param("usageId") Long usageId);

    @Query("SELECT a FROM Asset a WHERE a.product.id = :usageId AND a.isPrimary = :isPrimary")
    Asset findByUsageIdAndIsPrimary(@Param("usageId") Long usageId, @Param("isPrimary") boolean isPrimary);
}
