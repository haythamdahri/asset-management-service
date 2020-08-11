package org.management.asset.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RiskScenario {

    private String id = UUID.randomUUID().toString();
    private String name;
    private String description;
    private boolean status;

    @CreatedDate
    private LocalDateTime identificationDate;

}
