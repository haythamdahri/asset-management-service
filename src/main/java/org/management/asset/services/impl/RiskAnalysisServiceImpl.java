package org.management.asset.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.management.asset.bo.*;
import org.management.asset.dao.AssetRepository;
import org.management.asset.dao.TypologyRepository;
import org.management.asset.dto.PageDTO;
import org.management.asset.dto.RiskAnalysisRequestDTO;
import org.management.asset.dto.RiskAnalysisResponseDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.helpers.PaginationHelper;
import org.management.asset.services.RiskAnalysisService;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    private TypologyRepository typologyRepository;

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

    /**
     * Save riskAnalysis for an asset
     * @param riskAnalysisRequest
     * @return RiskAnalysisResponseDTO
     */
    @Override
    public RiskAnalysisResponseDTO saveRiskAnalysis(RiskAnalysisRequestDTO riskAnalysisRequest) {
        try {
            final boolean riskAnalysisRequestIdNotExists = StringUtils.isEmpty(riskAnalysisRequest.getId()) ||
                    riskAnalysisRequest.getId() == null ||
                    StringUtils.equals(riskAnalysisRequest.getId(), "null") ||
                    StringUtils.equals(riskAnalysisRequest.getId(), "undefined");
            riskAnalysisRequest.setId(riskAnalysisRequestIdNotExists ? null : riskAnalysisRequest.getId());
            // Check if asset is changed
            if( riskAnalysisRequest.getId() != null && !riskAnalysisRequest.getAsset().equals(riskAnalysisRequest.getCurrentAsset()) ) {
                throw new BusinessException(Constants.CANNOT_UPDATE_RISK_ANALYSIS_ASSET);
            }
            // Get asset
            Asset asset = this.assetRepository.findById(riskAnalysisRequest.getAsset()).orElseThrow(BusinessException::new);
            // Get or create riskAnalysis
            Optional<RiskAnalysis> optionalRiskAnalysis = Optional.empty();
            if( riskAnalysisRequest.getId() != null ) {
                optionalRiskAnalysis = asset.getRiskAnalyzes().stream().filter(ra -> ra.getId().equals(riskAnalysisRequest.getId())).findFirst();
            }
            RiskAnalysis riskAnalysis;
            if( !optionalRiskAnalysis.isPresent() ) {
                riskAnalysis = new RiskAnalysis();
                riskAnalysis.setIdentificationDate(LocalDateTime.now(ZoneId.of("UTC+1")));
            } else {
                riskAnalysis = optionalRiskAnalysis.get();
            }
            // Set data
            riskAnalysis.setProbability(riskAnalysisRequest.getProbability());
            riskAnalysis.setFinancialImpact(riskAnalysisRequest.getFinancialImpact());
            riskAnalysis.setOperationalImpact(riskAnalysisRequest.getOperationalImpact());
            riskAnalysis.setReputationalImpact(riskAnalysisRequest.getReputationalImpact());
            riskAnalysis.setRiskTreatmentStrategy(riskAnalysisRequest.getRiskTreatmentStrategyType());
            riskAnalysis.setRiskTreatmentPlan(riskAnalysisRequest.getRiskTreatmentPlan());
            riskAnalysis.setTargetFinancialImpact(riskAnalysisRequest.getTargetFinancialImpact());
            riskAnalysis.setTargetOperationalImpact(riskAnalysisRequest.getTargetOperationalImpact());
            riskAnalysis.setTargetReputationalImpact(riskAnalysisRequest.getTargetReputationalImpact());
            riskAnalysis.setTargetProbability(riskAnalysisRequest.getTargetProbability());
            riskAnalysis.setAcceptableResidualRisk(riskAnalysisRequest.getAcceptableResidualRisk());
            riskAnalysis.setStatus(riskAnalysisRequest.isStatus());
            // Get typology
            if( riskAnalysisRequest.getTypology() != null ) {
                Typology typology = this.typologyRepository.findById(riskAnalysisRequest.getTypology()).orElseThrow(BusinessException::new);
                // Set Threat
                Optional<Threat> optionalThreat = typology.getThreats().stream().filter(threat -> threat.getId().equals(riskAnalysisRequest.getThreat())).findFirst();
                optionalThreat.ifPresent(riskAnalysis::setThreat);
                // Set Vulnerability
                Optional<Vulnerability> optionalVulnerability = typology.getVulnerabilities().stream().filter(vulnerability -> vulnerability.getId().equals(riskAnalysisRequest.getVulnerability())).findFirst();
                optionalVulnerability.ifPresent(riskAnalysis::setVulnerability);
                // Set RiskScenario
                Optional<RiskScenario> optionalRiskScenario = typology.getRiskScenarios().stream().filter(riskScenario -> riskScenario.getId().equals(riskAnalysisRequest.getRiskScenario())).findFirst();
                optionalRiskScenario.ifPresent(riskAnalysis::setRiskScenario);
            }
            // Add riskAnalysis to the asset and Save
            asset.addRiskAnalysis(riskAnalysis);
            this.assetRepository.save(asset);
            // Return RiskAnalysisResponse
            return new RiskAnalysisResponseDTO(asset.getId(), asset.getName(), riskAnalysis);
        } catch (BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex.getMessage());
        }
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
    @Async
    public CompletableFuture<Void> setNullRiskAnalysisThreat(String typologyId, Threat threat) {
        List<Asset> assets = this.assetRepository.findAssetsByTypology_Id(typologyId);
        if( assets != null ) {
            assets.forEach(asset -> {
                AtomicReference<RiskAnalysis> riskAnalysisAtomicReference = new AtomicReference<>(null);
                if( asset != null && asset.getRiskAnalyzes() != null ) {
                    asset.getRiskAnalyzes().forEach(riskAnalysis -> {
                        if( riskAnalysis.getThreat() != null && riskAnalysis.getThreat().getId().equals(threat.getId()) ) {
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
    @Async
    public CompletableFuture<Void> setNullRiskAnalysisRiskScenario(String typologyId, RiskScenario riskScenario) {
        List<Asset> assets = this.assetRepository.findAssetsByTypology_Id(typologyId);
        if( assets != null ) {
            assets.forEach(asset -> {
                AtomicReference<RiskAnalysis> riskAnalysisAtomicReference = new AtomicReference<>(null);
                if( asset != null && asset.getRiskAnalyzes() != null ) {
                    asset.getRiskAnalyzes().forEach(riskAnalysis -> {
                        if( riskAnalysis.getRiskScenario() != null && riskAnalysis.getRiskScenario().getId().equals(riskScenario.getId()) ) {
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
    @Async
    public CompletableFuture<Void> setNullRiskAnalysisVulnerability(String typologyId, Vulnerability vulnerability) {
        List<Asset> assets = this.assetRepository.findAssetsByTypology_Id(typologyId);
        if( assets != null ) {
            assets.forEach(asset -> {
                AtomicReference<RiskAnalysis> riskAnalysisAtomicReference = new AtomicReference<>(null);
                if( asset != null && asset.getRiskAnalyzes() != null ) {
                    asset.getRiskAnalyzes().forEach(riskAnalysis -> {
                        if( riskAnalysis.getVulnerability() != null && riskAnalysis.getVulnerability().getId().equals(vulnerability.getId()) ) {
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
