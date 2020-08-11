package org.management.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.management.asset.bo.RiskTreatmentStrategyType;

import java.io.Serializable;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskAnalysisRequestDTO implements Serializable {
    private static final long serialVersionUID = -3058237496189905611L;

    private String asset;
    private String currentAsset;
    private String id;
    private String typology;
    private int probability;
    private int financialImpact;
    private int operationalImpact;
    private int reputationalImpact;
    private RiskTreatmentStrategyType riskTreatmentStrategyType;
    private String riskTreatmentPlan;
    private int targetFinancialImpact;
    private int targetOperationalImpact;
    private int targetReputationalImpact;
    private int targetProbability;
    private int acceptableResidualRisk;
    private boolean status;

    private String threat;
    private String vulnerability;
    private String riskScenario;

}
