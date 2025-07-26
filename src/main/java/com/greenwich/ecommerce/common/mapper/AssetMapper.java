package com.greenwich.ecommerce.common.mapper;

import com.greenwich.ecommerce.dto.request.AssetRequest;
import com.greenwich.ecommerce.dto.response.AssetResponse;
import com.greenwich.ecommerce.entity.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AssetMapper {

//    @Mapping(source = "product.id", target = "productId")
//    AssetResponse toAssetResponse(Asset asset);

    Asset toAssetEntity(AssetRequest request);
}
