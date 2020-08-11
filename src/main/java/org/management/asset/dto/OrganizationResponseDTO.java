package org.management.asset.dto;

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
public class OrganizationResponseDTO implements Serializable {
    private static final long serialVersionUID = 6737236390619240321L;

    private String id;
    private String name;

}
