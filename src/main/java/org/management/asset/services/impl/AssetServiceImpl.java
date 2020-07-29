package org.management.asset.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.management.asset.bo.Asset;
import org.management.asset.bo.Process;
import org.management.asset.bo.RiskAnalysis;
import org.management.asset.dao.AssetRepository;
import org.management.asset.dto.RiskAnalysisResponseDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.services.AssetService;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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
        Optional<Asset> assetOptional = this.assetRepository.findById(id);
        if (assetOptional.isPresent()) {
            Asset asset = assetOptional.get();
            if (asset.getRiskAnalyzes() != null) {
                asset.setRiskAnalyzes(asset.getRiskAnalyzes().stream().peek(riskAnalysis -> {
                    riskAnalysis.calculateGeneratedValues(asset);
                }).collect(Collectors.toSet()));
            }
        }
        return null;
    }

    @Override
    public RiskAnalysisResponseDTO getAssetRiskAnalysis(String assetId, String riskAnalysisId) {
        try {
            // Get asset
            Asset asset = this.assetRepository.findById(assetId).orElseThrow(BusinessException::new);
            // Get RiskAnalysis
            Optional<RiskAnalysis> optionalRiskAnalysis = asset.getRiskAnalyzes().stream().filter(riskAnalysis -> riskAnalysis.getId().equals(riskAnalysisId)).findFirst();
            return optionalRiskAnalysis.map(riskAnalysis -> new RiskAnalysisResponseDTO(assetId, asset.getName(), riskAnalysis)).orElse(null);
        } catch (BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
    }

    @Override
    public RiskAnalysis updateAssetRiskAnalysisStatus(String assetId, String riskAnalysisId, boolean status) {
        try {
            AtomicReference<RiskAnalysis> riskAnalysisAtomicReference = new AtomicReference<>(null);
            // Get Asset
            Asset asset = this.assetRepository.findById(assetId).orElseThrow(BusinessException::new);
            // Update RiskAnalysis status
            asset.setRiskAnalyzes(asset.getRiskAnalyzes().stream().peek(riskAnalysis -> {
                if (StringUtils.equals(riskAnalysisId, riskAnalysis.getId())) {
                    riskAnalysis.setStatus(status);
                    riskAnalysisAtomicReference.set(riskAnalysis);
                }
            }).collect(Collectors.toSet()));
            // Save Asset if threat found
            if (riskAnalysisAtomicReference.get() != null) {
                this.assetRepository.save(asset);
                return riskAnalysisAtomicReference.get();
            }
            // Throw BusinessException if not threat found
            throw new BusinessException(Constants.NO_RISK_SCENARIO_FOUND);
        } catch (BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
    }

    @Override
    public Asset updateAssetStatus(String id, boolean status) {
        Asset asset = this.assetRepository.findById(id).orElseThrow(BusinessException::new);
        asset.setStatus(status);
        return this.assetRepository.save(asset);
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

    @Override
    public Page<Asset> getAssets(String name, int page, int size) {
        return this.assetRepository.findByNameContainingIgnoreCase(name, PageRequest.of(page, size, Sort.Direction.ASC, "id"));
    }

    @Override
    public Asset updateAssetClassificationStatus(String id, boolean status) {
        Asset asset = this.assetRepository.findById(id).orElseThrow(BusinessException::new);
        if (asset.getClassification() != null) {
            asset.getClassification().setStatus(status);
        }
        return this.assetRepository.save(asset);
    }
}
