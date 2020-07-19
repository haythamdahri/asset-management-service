package org.management.asset.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Set;

/**
 * @author Haytham DAHRI
 */
@Document(collection = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company implements Serializable {

    private static final long serialVersionUID = 6911109441298292437L;

    @Id
    private String id;
    private String name;

    private AssetFile image;

    @DBRef
    private Set<User> users;

}
