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
public class ProcessDTO implements Serializable {

    private static final long serialVersionUID = 4868470138606001745L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

}
