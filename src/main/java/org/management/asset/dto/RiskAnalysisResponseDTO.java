package org.management.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.management.asset.bo.RiskAnalysis;

import java.io.Serializable;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskAnalysisResponseDTO implements Serializable {
    private static final long serialVersionUID = -1706164743194545373L;

    private String assetId;

    private String assetName;

    private RiskAnalysis riskAnalysis;

}
