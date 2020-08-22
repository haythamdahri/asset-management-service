package org.management.asset.services;

import org.management.asset.bo.RiskScenario;
import org.management.asset.bo.Threat;
import org.management.asset.bo.Vulnerability;
import org.management.asset.dto.PageDTO;
import org.management.asset.dto.RiskAnalysisRequestDTO;
import org.management.asset.dto.RiskAnalysisResponseDTO;

import java.util.concurrent.CompletableFuture;

/**
 * @author Haytham DAHRI
 */
public interface RiskAnalysisService {


    PageDTO<RiskAnalysisResponseDTO> getRiskAnalyzes(String assetId, int page, int size, String direction, String... sort);

    Integer getRiskAnalyzesCounter();

    RiskAnalysisResponseDTO saveRiskAnalysis(RiskAnalysisRequestDTO riskAnalysisRequest);

    CompletableFuture<Void> updateRiskAnalysisThreat(String typologyId, Threat threat);

    CompletableFuture<Void> updateRiskAnalysisRiskScenario(String typologyId, RiskScenario riskScenario);

    CompletableFuture<Void> updateRiskAnalysisVulnerability(String typologyId, Vulnerability vulnerability);

    CompletableFuture<Void> setNullRiskAnalysisThreat(String typologyId, Threat threat);

    CompletableFuture<Void> setNullRiskAnalysisRiskScenario(String typologyId, RiskScenario riskScenario);

    CompletableFuture<Void> setNullRiskAnalysisVulnerability(String typologyId, Vulnerability vulnerability);

    void setNullRiskAnalysisThreatSynchronously(String typologyId, Threat threat);

    void setNullRiskAnalysisRiskScenarioSynchronously(String typologyId, RiskScenario riskScenario);

    void setNullRiskAnalysisVulnerabilitySynchronously(String typologyId, Vulnerability vulnerability);
}
