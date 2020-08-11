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
public class PageableDTO implements Serializable {
    private static final long serialVersionUID = 6858415462955943690L;

    private int pageSize;

    private int pageNumber;

}
