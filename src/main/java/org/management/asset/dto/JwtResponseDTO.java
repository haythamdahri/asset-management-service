package org.management.asset.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * @author Haytam DAHRI
 */
public class JwtResponseDTO implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;

    @JsonProperty("jwttoken")
    private final String jwttoken;

    /**
     * @param jwttoken
     */
    public JwtResponseDTO(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    /**
     * @return jwttoken
     */
    public String getToken() {
        return this.jwttoken;
    }
}
