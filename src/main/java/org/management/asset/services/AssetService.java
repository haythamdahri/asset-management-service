package org.management.asset.services;

import org.management.asset.bo.Asset;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Haytham DAHRI
 * Spécification du service des actifs
 */
public interface AssetService {

    Asset saveAsset(Asset asset);

    boolean deleteAsset(String id);

    Asset getAsset(String id);

    List<Asset> getAssets();

    Long getAssetsCounter();

    Page<Asset> getAssets(int page, int size);

}
