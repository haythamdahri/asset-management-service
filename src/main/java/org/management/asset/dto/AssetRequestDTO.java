package org.management.asset.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @author Haytham DAHRI
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetRequestDTO implements Serializable {
    private static final long serialVersionUID = -7380240007939993592L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("location")
    private String location;

    @JsonProperty("process")
    private String process;

    @JsonProperty("owner")
    private String owner;

    @JsonProperty("typology")
    private String typology;

    @JsonProperty("confidentiality")
    private int confidentiality;

    @JsonProperty("availability")
    private int availability;

    @JsonProperty("integrity")
    private int integrity;

    @JsonProperty("traceability")
    private int traceability;

    @JsonProperty("classificationStatus")
    private boolean classificationStatus;

    @JsonProperty("updateImage")
    private boolean updateImage;

    @JsonProperty("file")
    private MultipartFile file;

}
