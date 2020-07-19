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
@Document(collection = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department implements Serializable {

    private static final long serialVersionUID = 3021098015902959779L;

    @Id
    private String id;
    private String name;

    @DBRef
    private Set<User> users;

}
