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
public class AssetDTO implements Serializable {
    private static final long serialVersionUID = -6145998597717232382L;

    private String id;

    private String name;

}
