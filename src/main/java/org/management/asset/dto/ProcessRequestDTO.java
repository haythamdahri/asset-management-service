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
public class ProcessRequestDTO implements Serializable {

    private static final long serialVersionUID = 3918984178237913698L;

    @JsonProperty("id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private boolean status;

    @JsonProperty("classificationStatus")
    private boolean classificationStatus;

    @JsonProperty("parentProcess")
    private String parentProcess;

    @JsonProperty("organization")
    private String organization;

    @JsonProperty("confidentiality")
    private int confidentiality;

    @JsonProperty("availability")
    private int availability;

    @JsonProperty("integrity")
    private int integrity;

    @JsonProperty("traceability")
    private int traceability;

    @JsonProperty("updateClassificationStatus")
    private boolean updateClassificationStatus;



}
