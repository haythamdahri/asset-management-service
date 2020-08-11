package org.management.asset.dto;

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
public class ClassificationResponseDTO implements Serializable {
    private static final long serialVersionUID = -2665442044805195091L;

    private List<Integer> confidentialities;
    private List<Integer> availabilities;
    private List<Integer> integrities;
    private List<Integer> traceabilities;

}
