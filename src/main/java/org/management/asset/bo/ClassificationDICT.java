package org.management.asset.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationDICT implements Serializable {

    private static final long serialVersionUID = -1459913926470809020L;

    private String id = UUID.randomUUID().toString();
    // Confidentialité
    private int confidentiality;
    // Disponibilité
    private int availability;
    // Intégrité
    private int integrity;
    // Traçabilité
    private int traceability;

    @Transient
    private int score;
    private boolean status;
    private LocalDateTime identificationDate;

    @PostConstruct
    private void postConstruct() {
        // Calculate score
        this.score = this.confidentiality + this.availability + this.traceability + this.integrity;
    }

}
