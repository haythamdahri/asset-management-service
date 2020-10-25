package org.management.asset.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Document(collection = "settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Setting implements Serializable {
    private static final long serialVersionUID = 2358525850342713744L;

    @Id
    private String id;
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime identificationDate;
    private List<Integer> probabilities;
    private List<Integer> financialImpacts;
    private List<Integer> operationalImpacts;
    private List<Integer> reputationalImpacts;
    private List<Integer> targetFinancialImpacts;
    private List<Integer> targetOperationalImpacts;
    private List<Integer> targetReputationalImpacts;
    private List<Integer> targetProbabilities;
    private List<Integer> acceptableResidualRisks;
    
    private List<Integer> confidentialities;
    private List<Integer> availabilities;
    private List<Integer> integrities;
    private List<Integer> traceabilities;

    private Integer maxAttemptsWithoutCaptcha;


}
