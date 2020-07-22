package org.management.asset.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO implements Serializable {

    private static final long serialVersionUID = -2981072330123444311L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("roles")
    private List<String> roles;

    @JsonProperty("updatePermissions")
    private boolean updatePermissions;
}
