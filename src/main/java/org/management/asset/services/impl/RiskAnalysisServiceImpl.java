package org.management.asset.services.impl;

import org.management.asset.bo.*;
import org.management.asset.dao.AssetRepository;
import org.management.asset.dto.PageDTO;
import org.management.asset.services.RiskAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Haytham DAHRI
 */
@Service
public class RiskAnalysisServiceImpl implements RiskAnalysisService {

    @Autowired
    private AssetRepository assetRepository;


    @Override
    public PageDTO<RiskAnalysis> getRiskAnalysis(String name, int page, int size) {
        return null;
    }

    @Override
    @Async
    public CompletableFuture<Void> updateRiskAnalysisThreat(String typologyId, Threat threat) {
        List<Asset> assets = this.assetRepository.findAssetsByTypology_Id(typologyId);
        if( assets != null ) {
            assets.forEach(asset -> {
                AtomicReference<RiskAnalysis> riskAnalysisAtomicReference = new AtomicReference<>(null);
                if( asset != null && asset.getRiskAnalyses() != null ) {
                    asset.getRiskAnalyses().forEach(riskAnalysis -> {
                        if( riskAnalysis.getThreat().getId().equals(threat.getId()) ) {
                            riskAnalysis.setThreat(threat);
                            riskAnalysisAtomicReference.set(riskAnalysis);
                        }
                    });
                    // Check if riskAnalysis is assigned
                    if( riskAnalysisAtomicReference.get() != null) {
                        this.assetRepository.save(asset);
                    }
                }
            });
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> updateRiskAnalysisRiskScenario(String typologyId, RiskScenario riskScenario) {
        List<Asset> assets = this.assetRepository.findAssetsByTypology_Id(typologyId);
        if( assets != null ) {
            assets.forEach(asset -> {
                AtomicReference<RiskAnalysis> riskAnalysisAtomicReference = new AtomicReference<>(null);
                if( asset != null && asset.getRiskAnalyses() != null ) {
                    asset.getRiskAnalyses().forEach(riskAnalysis -> {
                        if( riskAnalysis.getRiskScenario().getId().equals(riskScenario.getId()) ) {
                            riskAnalysis.setRiskScenario(riskScenario);
                            riskAnalysisAtomicReference.set(riskAnalysis);
                        }
                    });
                    // Check if riskAnalysis is assigned
                    if( riskAnalysisAtomicReference.get() != null) {
                        this.assetRepository.save(asset);
                    }
                }
            });
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Async
    public CompletableFuture<Void> updateRiskAnalysisVulnerability(String typologyId, Vulnerability vulnerability) {
        List<Asset> assets = this.assetRepository.findAssetsByTypology_Id(typologyId);
        if( assets != null ) {
            assets.forEach(asset -> {
                AtomicReference<RiskAnalysis> riskAnalysisAtomicReference = new AtomicReference<>(null);
                if( asset != null && asset.getRiskAnalyses() != null ) {
                    asset.getRiskAnalyses().forEach(riskAnalysis -> {
                        if( riskAnalysis.getVulnerability().getId().equals(vulnerability.getId()) ) {
                            riskAnalysis.setVulnerability(vulnerability);
                            riskAnalysisAtomicReference.set(riskAnalysis);
                        }
                    });
                    // Check if riskAnalysis is assigned
                    if( riskAnalysisAtomicReference.get() != null) {
                        this.assetRepository.save(asset);
                    }
                }
            });
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> setNullRiskAnalysisThreat(String typologyId, Threat threat) {
        List<Asset> assets = this.assetRepository.findAssetsByTypology_Id(typologyId);
        if( assets != null ) {
            assets.forEach(asset -> {
                AtomicReference<RiskAnalysis> riskAnalysisAtomicReference = new AtomicReference<>(null);
                if( asset != null && asset.getRiskAnalyses() != null ) {
                    asset.getRiskAnalyses().forEach(riskAnalysis -> {
                        if( riskAnalysis.getThreat().getId().equals(threat.getId()) ) {
                            riskAnalysis.setThreat(null);
                            riskAnalysisAtomicReference.set(riskAnalysis);
                        }
                    });
                    // Check if riskAnalysis is assigned
                    if( riskAnalysisAtomicReference.get() != null) {
                        this.assetRepository.save(asset);
                    }
                }
            });
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> setNullRiskAnalysisRiskScenario(String typologyId, RiskScenario riskScenario) {
        List<Asset> assets = this.assetRepository.findAssetsByTypology_Id(typologyId);
        if( assets != null ) {
            assets.forEach(asset -> {
                AtomicReference<RiskAnalysis> riskAnalysisAtomicReference = new AtomicReference<>(null);
                if( asset != null && asset.getRiskAnalyses() != null ) {
                    asset.getRiskAnalyses().forEach(riskAnalysis -> {
                        if( riskAnalysis.getRiskScenario().getId().equals(riskScenario.getId()) ) {
                            riskAnalysis.setRiskScenario(null);
                            riskAnalysisAtomicReference.set(riskAnalysis);
                        }
                    });
                    // Check if riskAnalysis is assigned
                    if( riskAnalysisAtomicReference.get() != null) {
                        this.assetRepository.save(asset);
                    }
                }
            });
        }
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public CompletableFuture<Void> setNullRiskAnalysisVulnerability(String typologyId, Vulnerability vulnerability) {
        List<Asset> assets = this.assetRepository.findAssetsByTypology_Id(typologyId);
        if( assets != null ) {
            assets.forEach(asset -> {
                AtomicReference<RiskAnalysis> riskAnalysisAtomicReference = new AtomicReference<>(null);
                if( asset != null && asset.getRiskAnalyses() != null ) {
                    asset.getRiskAnalyses().forEach(riskAnalysis -> {
                        if( riskAnalysis.getVulnerability().getId().equals(vulnerability.getId()) ) {
                            riskAnalysis.setVulnerability(null);
                            riskAnalysisAtomicReference.set(riskAnalysis);
                        }
                    });
                    // Check if riskAnalysis is assigned
                    if( riskAnalysisAtomicReference.get() != null) {
                        this.assetRepository.save(asset);
                    }
                }
            });
        }
        return CompletableFuture.completedFuture(null);
    }
}
