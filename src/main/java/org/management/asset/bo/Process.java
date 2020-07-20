package org.management.asset.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author Haytham DAHRI
 */
@Document(collection = "processes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Process implements Serializable {

    private static final long serialVersionUID = -8923997747362630547L;

    @Id
    private String id;
    private String name;
    private String description;
    private boolean status;

    @DBRef
    private Process parentProcess;
    private ClassificationDICT classificationDICT;

    /**
     * Actifs
     */
    @DBRef
    private Set<Asset> assets;

}
