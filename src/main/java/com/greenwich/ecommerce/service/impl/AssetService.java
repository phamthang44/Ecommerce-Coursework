package com.greenwich.ecommerce.service.impl;

import com.greenwich.ecommerce.dto.request.AssetRequest;
import com.greenwich.ecommerce.dto.response.AssetResponse;
import com.greenwich.ecommerce.entity.Asset;
import com.greenwich.ecommerce.entity.Product;
import com.greenwich.ecommerce.exception.InvalidDataException;
import com.greenwich.ecommerce.exception.ResourceNotFoundException;
import com.greenwich.ecommerce.infra.cloundinary.CloudinaryService;
import com.greenwich.ecommerce.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final CloudinaryService cloudinaryService;
    private final AssetRepository assetRepository;

    public Asset uploadAsset(AssetRequest request, MultipartFile file, Product product) {
        String url = cloudinaryService.uploadFile(file);

        Asset asset = new Asset();
        asset.setUrl(url);
        asset.setType(request.getType());
        asset.setUsage(request.getUsage());
        asset.setPrimary(request.isPrimary());
        asset.setAltText(request.getAltText());
        asset.setProduct(product);

        return assetRepository.save(asset);
    }

    public AssetResponse convertToAssetResponse(Asset asset) {
        return AssetResponse.builder()
                .id(asset.getId())
                .usage(asset.getUsage())
                .altText(asset.getAltText())
                .isPrimary(asset.isPrimary())
                .url(asset.getUrl())
                .build();
    }

    public List<AssetResponse> getAllAssets() {
        List<Asset> assets = assetRepository.findAll();
        return assets.stream()
                .map(this::convertToAssetResponse)
                .toList();
    }

    public List<AssetResponse> convertToListAssetResponse(List<Asset> assets) {
        return assets.stream()
                .map(this::convertToAssetResponse)
                .toList();
    }

    public Asset getAssetById(Long assetId) {
        return assetRepository.findById(assetId)
                .orElseThrow(() -> new ResourceNotFoundException("Asset not found with id: " + assetId));
    }

    public Asset getAssetByUsageId(Long usageId) {
        if (usageId == null || usageId <= 0) {
            throw new InvalidDataException("Usage ID must be a positive number");
        }
        return assetRepository.findByUsageId(usageId);
    }

}
