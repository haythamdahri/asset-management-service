package org.management.asset.services;

import org.management.asset.bo.RiskScenario;
import org.management.asset.bo.Threat;
import org.management.asset.bo.Typology;
import org.management.asset.bo.Vulnerability;
import org.management.asset.dto.ThreatResponseDTO;
import org.management.asset.dto.TypologyRequestDTO;
import org.management.asset.dto.TypologyResponseDTO;
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

    ThreatResponseDTO getTypologyThreatStatus(String typologyId, String threatId);

    boolean deleteTypologyThreatStatus(String typologyId, String threatId);

    Vulnerability updateTypologyVulnerabilityStatus(String typologyId, String vulnerabilityId, boolean status);

    RiskScenario updateTypologyRiskScenarioStatus(String typologyId, String riskScenarioId, boolean status);

    Page<Typology> getTypologies(int page, int size);

    Page<Typology> getTypologies(String name, int page, int size);

}
