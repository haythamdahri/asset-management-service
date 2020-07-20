package org.management.asset.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Document(collection = "typologies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Typology {

    @Id
    private String id;
    private String name;
    private String description;

    // Menaces
    private List<Threat> threats;
    // Vulnérabilités
    private List<Vulnerability> vulnerabilities;
    // Scénarios Risques
    private List<RiskScenario> riskScenarios;

}
