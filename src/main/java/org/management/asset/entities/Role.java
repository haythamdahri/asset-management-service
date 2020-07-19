package org.management.asset.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Haytam DAHRI
 */
@Document(collection = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

    private static final long serialVersionUID = 6912880882655635525L;

    @Id
    private String id;

    @Enumerated(EnumType.STRING)
    private RoleType roleName;

    @DBRef
    @JsonIgnore
    private Set<User> users;

    @DBRef
    @JsonIgnore
    private Set<Group> groups;

}
