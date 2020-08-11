package org.management.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.management.asset.bo.RiskScenario;
import org.management.asset.bo.Threat;

import java.io.Serializable;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskScenarioResponseDTO implements Serializable {
    private static final long serialVersionUID = -1706164743194545373L;

    private String typologyId;

    private String typologyName;

    private RiskScenario riskScenario;

}
