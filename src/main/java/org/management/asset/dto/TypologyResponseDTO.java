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
public class TypologyResponseDTO implements Serializable {
    private static final long serialVersionUID = -4260273509297228206L;

    private String id;

    private String name;

}
