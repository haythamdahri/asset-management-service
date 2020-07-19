package org.management.asset.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetFile implements Serializable {

    private static final long serialVersionUID = -5593733910696898093L;

    @Field(name = "name")
    private String name;

    @Field(name = "extension")
    private String extension;

    @Field(name = "media_type")
    private String mediaType;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private byte[] file;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime creationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime updateDate;

}
