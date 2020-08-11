package org.management.asset.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationDICT implements Serializable {

    private static final long serialVersionUID = -1459913926470809020L;

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

    @CreatedDate
    private LocalDateTime identificationDate;

    public ClassificationDICT(int confidentiality, int availability, int integrity, int traceability, boolean status) {
        this.confidentiality = confidentiality;
        this.availability = availability;
        this.integrity = integrity;
        this.traceability = traceability;
        this.status = status;
    }

    public ClassificationDICT(LocalDateTime identificationDate) {
        this.identificationDate = identificationDate;
    }

    public void postConstruct() {
        // Calculate score
        this.score = this.confidentiality + this.availability + this.traceability + this.integrity;
    }

}
