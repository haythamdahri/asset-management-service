package org.management.asset.services;

import org.management.asset.bo.RiskScenario;
import org.management.asset.bo.Threat;
import org.management.asset.bo.Typology;
import org.management.asset.bo.Vulnerability;
import org.management.asset.dto.*;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Haytham DAHRI
 * Spécification du service des typlogies
 */
public interface TypologyService {

    Typology saveTypology(Typology typology);

    Typology saveTypology(TypologyRequestDTO typologyRequest);

    boolean deleteTypology(String id);

    Typology getTypology(String id);

    List<Typology> getTypologies();

    List<TypologyResponseDTO> getCustomTypologies();

    Threat updateTypologyThreatStatus(String typologyId, String threatId, boolean status);

    ThreatResponseDTO getTypologyThreat(String typologyId, String threatId);

    VulnerabilityResponseDTO getTypologyVulnerability(String typologyId, String vulnerabilityId);

    RiskScenarioResponseDTO getTypologyRiskScenario(String typologyId, String riskScenarioId);

    boolean deleteTypologyThreat(String typologyId, String threatId);

    boolean deleteTypologyVulnerability(String typologyId, String vulnerabilityId);

    boolean deleteTypologyRiskScenario(String typologyId, String riskScenarioId);

    Vulnerability updateTypologyVulnerabilityStatus(String typologyId, String vulnerabilityId, boolean status);

    RiskScenario updateTypologyRiskScenarioStatus(String typologyId, String riskScenarioId, boolean status);

    Page<Typology> getTypologies(int page, int size);

    long getTypologiesCounter();

    Page<Typology> getTypologies(String name, int page, int size, String direction, String... sort);

}
