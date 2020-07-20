package org.management.asset.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import javax.annotation.PostConstruct;
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

    private String id;
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
    private String targetFinancialImpact;
    // Impact Operationnel Cible
    private String tagetOperationalImpact;
    // Impact Reputationnel Cible
    private String targetReputationalImpact;
    // Probabilité Cibe
    private int targetProbability;
    // Risque Résiduel Acceptable
    private int acceptableResidualRisk;
    // Statut
    private boolean status;
    // Date d'identification
    private LocalDateTime identificationDate;
    /**
     * Champ a récuperer en se basant sur la typologie de l'actif
     * Field to retrieve based on asset typology
     * Choose value from Typology lists (threats, vulnerabilities, riskScenarios)
     */
    private Threat threat;
    private Vulnerability vulnerability;
    private RiskScenario riskScenario;

    @PostConstruct
    private void postConstruct() {
        // Fill id
        if( this.id == null ) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public void calculateGeneratedValues(Asset asset) {
        // Set impact value
        this.impact = Stream.of(this.financialImpact, this.operationalImpact, this.reputationalImpact).mapToInt(v -> v).max().orElse(0);
        // Set exposition value
        this.exposition = this.impact * this.probability;
        // Set grossValue
        this.grossValue = this.exposition * asset.getClassificationDICT().getScore();
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
