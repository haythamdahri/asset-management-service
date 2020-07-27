package org.management.asset.services;

import org.management.asset.bo.Threat;
import org.management.asset.dto.PageDTO;
import org.management.asset.dto.ThreatRequestDTO;
import org.management.asset.dto.ThreatResponseDTO;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface ThreatService {

    List<Threat> getThreats();

    ThreatResponseDTO saveThreat(ThreatRequestDTO threatRequest);

    PageDTO<ThreatResponseDTO> getThreats(String name, int page, int size);

}
