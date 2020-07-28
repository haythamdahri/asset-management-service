package org.management.asset.services;

import org.management.asset.bo.RiskScenario;
import org.management.asset.dto.PageDTO;
import org.management.asset.dto.RiskScenarioRequestDTO;
import org.management.asset.dto.RiskScenarioResponseDTO;
import org.management.asset.dto.VulnerabilityRequestDTO;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface RiskScenarioService {

    List<RiskScenario> getRiskScenarios();

    RiskScenarioResponseDTO saveRiskScenario(RiskScenarioRequestDTO riskScenarioRequest);

    PageDTO<RiskScenarioResponseDTO> getRiskScenarios(String name, int page, int size);

}