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
public class EntityRequestDTO implements Serializable {
    private static final long serialVersionUID = -4735043920828599804L;

    private String id;
    private String name;
    private String organization;


}
