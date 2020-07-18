package org.management.asset.bo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Haytam DAHRI
 */
@Entity
@Table(name = "roles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", unique = true, updatable = false)
    private RoleType roleName;

    @EqualsAndHashCode.Exclude
    @ManyToMany(targetEntity = User.class, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnore
    private Set<User> users;

    @EqualsAndHashCode.Exclude
    @ManyToMany(targetEntity = Group.class, mappedBy = "roles", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JsonIgnore
    private Set<Group> groups;


}
