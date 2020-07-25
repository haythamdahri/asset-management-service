package org.management.asset.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author Haytham DAHRI
 */
@Document(collection = "organizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization implements Serializable {

    private static final long serialVersionUID = 6911109441298292437L;

    @Id
    private String id;

    @Indexed(name = "name", unique = true)
    private String name;
    private String description;
    private AssetFile image;

}
