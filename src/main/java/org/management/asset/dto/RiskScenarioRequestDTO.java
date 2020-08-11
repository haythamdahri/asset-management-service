package org.management.asset.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class RiskScenarioRequestDTO implements Serializable {
    private static final long serialVersionUID = -5628330134351145289L;

    @JsonProperty("currentTypology")
    private String currentTypology;

    @JsonProperty("typology")
    private String typology;

    @JsonProperty("riskScenario")
    private String riskScenario;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private boolean status;

}
