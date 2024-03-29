package org.management.asset.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Transient;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskAnalysis {

    private String id = UUID.randomUUID().toString();
    private int probability;
    private int financialImpact;
    private int operationalImpact;
    private int reputationalImpact;
    // Impact
    @Transient
    private int impact;
    // Expositon
    @Transient
    private int exposition;
    // Valeur Brut
    @Transient
    private int grossValue;
    // Analyse des risque
    @Transient
    private RiskType riskAnalysis;
    // Stratégie de traitement des risques
    private RiskTreatmentStrategyType riskTreatmentStrategy;
    // Plan de traitement de risque
    private String riskTreatmentPlan;
    // Impact Financier Cible
    private int targetFinancialImpact;
    // Impact Operationnel Cible
    private int targetOperationalImpact;
    // Impact Reputationnel Cible
    private int targetReputationalImpact;
    // Probabilité Cible
    private int targetProbability;
    // Risque Résiduel Acceptable
    private int acceptableResidualRisk;
    // Statut
    private Boolean status;
    // Date d'identification
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime identificationDate;
    /**
     * Champ a récuperer en se basant sur la typologie de l'actif
     * Field to retrieve based on asset typology
     * Choose value from Typology lists (threats, vulnerabilities, riskScenarios)
     */
    private Threat threat;
    private Vulnerability vulnerability;
    private RiskScenario riskScenario;

    public void calculateGeneratedValues(Asset asset) {
        // Set impact value
        this.impact = Stream.of(this.financialImpact, this.operationalImpact, this.reputationalImpact).mapToInt(v -> v).max().orElse(0);
        // Set exposition value
        this.exposition = this.impact * this.probability;
        // Set grossValue
        asset.getClassification().postConstruct();
        this.grossValue = this.exposition * asset.getClassification().getScore();
        // Set riskAnalysis value
        if (this.grossValue < 14) {
            this.riskAnalysis = RiskType.VERY_LOW;
        } else if (this.grossValue < 33) {
            this.riskAnalysis = RiskType.LOW;
        } else if (this.grossValue < 60) {
            this.riskAnalysis = RiskType.MEDIUM;
        } else {
            this.riskAnalysis = RiskType.HIGH;
        }
    }

}
