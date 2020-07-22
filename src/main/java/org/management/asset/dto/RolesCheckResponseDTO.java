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
public class RolesCheckResponseDTO implements Serializable {

    private static final long serialVersionUID = 8615083631841703610L;

    @JsonProperty("message")
    private String message;

    @JsonProperty("hasRole")
    private boolean hasRole;

}
