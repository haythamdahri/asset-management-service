package org.management.asset.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author Haytham DAHRI
 * Les Menaces
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Threat {

    private String id;
    private String description;
    private boolean status;
    private LocalDateTime identificationDate;

    @PostConstruct
    private void postConstruct() {
        // Assign id
        if( this.id == null ) {
            this.id = UUID.randomUUID().toString();
        }
    }

}
