package org.management.asset.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.management.asset.bo.*;
import org.management.asset.dao.AssetRepository;
import org.management.asset.dto.PageDTO;
import org.management.asset.dto.RiskAnalysisResponseDTO;
import org.management.asset.helpers.PaginationHelper;
import org.management.asset.services.RiskAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Haytham DAHRI
 */
@Service
public class RiskAnalysisServiceImpl implements RiskAnalysisService {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private PaginationHelper paginationHelper;

    @Override
    public PageDTO<RiskAnalysisResponseDTO> getRiskAnalyzes(String assetId, int page, int size) {
        List<RiskAnalysisResponseDTO> riskAnalyzes = new ArrayList<>();
        this.assetRepository.findAll().forEach(asset -> {
            if( asset.getRiskAnalyzes() != null ) {
                if(StringUtils.isNotEmpty(assetId) && asset.getId().equals(assetId)) {
                    asset.setRiskAnalyzes(asset.getRiskAnalyzes().stream().peek(riskAnalysis -> {
                        riskAnalysis.calculateGeneratedValues(asset);
                        RiskAnalysisResponseDTO riskAnalysisResponse = new RiskAnalysisResponseDTO(asset.getId(), asset.getName(), riskAnalysis);
                        riskAnalyzes.add(riskAnalysisResponse);
                    }).collect(Collectors.toSet()));
                } else if( StringUtils.isEmpty(assetId) ) {
                    asset.setRiskAnalyzes(asset.getRiskAnalyzes().stream().peek(riskAnalysis -> {
                        RiskAnalysisResponseDTO riskAnalysisResponse = new RiskAnalysisResponseDTO(asset.getId(), asset.getName(), riskAnalysis);
                        riskAnalyzes.add(riskAnalysisResponse);
                    }).collect(Collectors.toSet()));
                }
            }
        });
        // Pagination
        return this.paginationHelper.buildPage(page, size, riskAnalyzes);
    }

    @Override
    @Async
    public CompletableFuture<Void> updateRiskAnalysisThreat(String typologyId, Threat threat) {
        List<Asset> assets = this.assetRepository.findAssetsByTypology_Id(typologyId);
        if( assets != null ) {
            assets.forEach(asset -> {
                AtomicReference<RiskAnalysis> riskAnalysisAtomicReference = new AtomicReference<>(null);
                if( asset != null && asset.getRiskAnalyzes() != null ) {
                    asset.getRiskAnalyzes().forEach(riskAnalysis -> {
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
                if( asset != null && asset.getRiskAnalyzes() != null ) {
                    asset.getRiskAnalyzes().forEach(riskAnalysis -> {
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
                if( asset != null && asset.getRiskAnalyzes() != null ) {
                    asset.getRiskAnalyzes().forEach(riskAnalysis -> {
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
                if( asset != null && asset.getRiskAnalyzes() != null ) {
                    asset.getRiskAnalyzes().forEach(riskAnalysis -> {
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
                if( asset != null && asset.getRiskAnalyzes() != null ) {
                    asset.getRiskAnalyzes().forEach(riskAnalysis -> {
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
                if( asset != null && asset.getRiskAnalyzes() != null ) {
                    asset.getRiskAnalyzes().forEach(riskAnalysis -> {
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
