package org.management.asset.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Haytam DAHRI
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequestDTO implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;
}
