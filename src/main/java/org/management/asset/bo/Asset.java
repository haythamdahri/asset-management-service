package org.management.asset.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author Haytham DAHRI
 */
@Document(collection = "assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset implements Serializable {

    private static final long serialVersionUID = 7842938316877218007L;

    @Id
    private String id;
    private String name;
    private String description;
    private boolean status;
    private Location location;
    private LocalDateTime identificationDate;
    private ClassificationDICT classificationDICT;
    private Set<RiskAnalysis> riskAnalyses;

    @DBRef
    private User owner;

    @DBRef
    private Typology typology;

    @PostConstruct
    private void postConstruct() {
        // Loop through riskAnalyses and calculate generated values
        if( this.riskAnalyses != null ) {
            this.riskAnalyses.forEach(riskAnalysis -> riskAnalysis.calculateGeneratedValues(this));
        }
    }

}
