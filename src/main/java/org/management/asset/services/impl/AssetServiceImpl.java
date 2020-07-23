package org.management.asset.services.impl;

import org.management.asset.bo.Asset;
import org.management.asset.dao.AssetRepository;
import org.management.asset.services.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 * Implémentation du service des actifs
 */
@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Override
    public Asset saveAsset(Asset asset) {
        return this.assetRepository.save(asset);
    }

    @Override
    public boolean deleteAsset(String id) {
        this.assetRepository.deleteById(id);
        return this.assetRepository.findById(id).isPresent();
    }

    @Override
    public Asset getAsset(String id) {
        return this.assetRepository.findById(id).orElse(null);
    }

    @Override
    public List<Asset> getAssets() {
        return this.assetRepository.findAll();
    }

    @Override
    public Long getAssetsCounter() {
        return this.assetRepository.countAssets();
    }

    @Override
    public Page<Asset> getAssets(int page, int size) {
        return this.assetRepository.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "id"));
    }
}
