package org.management.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SettingRequestDTO implements Serializable {
    private static final long serialVersionUID = 3627316162274829755L;

    private Integer minProbability;
    private Integer maxProbability;
    private Integer maxFinancialImpact;
    private Integer minFinancialImpact;
    private Integer maxOperationalImpact;
    private Integer minOperationalImpact;
    private Integer maxReputationalImpact;
    private Integer minReputationalImpact;
    private Integer maxTargetFinancialImpact;
    private Integer minTargetFinancialImpact;
    private Integer maxTargetOperationalImpact;
    private Integer minTargetOperationalImpact;
    private Integer maxTargetReputationalImpact;
    private Integer minTargetReputationalImpact;
    private Integer minTargetProbability;
    private Integer maxTargetProbability;
    private Integer minAcceptableResidualRisk;
    private Integer maxAcceptableResidualRisk;
    private Integer maxConfidentiality;
    private Integer minConfidentiality;
    private Integer maxAvailability;
    private Integer minAvailability;
    private Integer maxIntegrity;
    private Integer minIntegrity;
    private Integer maxTraceability;
    private Integer minTraceability;

}
