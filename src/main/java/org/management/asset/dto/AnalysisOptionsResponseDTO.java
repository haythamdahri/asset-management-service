package org.management.asset.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisOptionsResponseDTO implements Serializable {
    private static final long serialVersionUID = 3745447415630299949L;

    private List<Integer> probabilities;
    private List<Integer> financialImpacts;
    private List<Integer> operationalImpacts;
    private List<Integer> reputationalImpacts;
    private List<Integer> targetFinancialImpacts;
    private List<Integer> targetOperationalImpacts;
    private List<Integer> targetReputationalImpacts;
    private List<Integer> targetProbabilities;
    private List<Integer> acceptableResidualRisks;

}
