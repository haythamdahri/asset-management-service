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
public class PasswordResetRequestDTO implements Serializable {

    private static final long serialVersionUID = -942197584457959986L;

    @JsonProperty("password")
    private String password;

    @JsonProperty("token")
    private String token;

}
