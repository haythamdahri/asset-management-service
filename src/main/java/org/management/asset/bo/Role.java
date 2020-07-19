package org.management.asset.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

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

    private RoleType roleName;

    @DBRef
    @JsonIgnore
    private Set<User> users;

    @DBRef
    @JsonIgnore
    private Set<Group> groups;

}
