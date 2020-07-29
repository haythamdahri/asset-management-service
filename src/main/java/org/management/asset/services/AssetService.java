package org.management.asset.services;

import org.management.asset.bo.Asset;
import org.management.asset.bo.RiskAnalysis;
import org.management.asset.dto.RiskAnalysisResponseDTO;
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

    RiskAnalysisResponseDTO getAssetRiskAnalysis(String assetId, String riskAnalysisId);

    RiskAnalysis updateAssetRiskAnalysisStatus(String assetId, String riskAnalysisId, boolean status);

    Asset updateAssetStatus(String id, boolean status);

    Asset updateAssetClassificationStatus(String id, boolean status);

    List<Asset> getAssets();

    Long getAssetsCounter();

    Page<Asset> getAssets(int page, int size);

    Page<Asset> getAssets(String name, int page, int size);

}
