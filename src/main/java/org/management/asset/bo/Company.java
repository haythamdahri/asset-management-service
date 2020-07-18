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
 * @author Haytham DAHRI
 */
@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company implements Serializable {

    private static final long serialVersionUID = 6911109441298292437L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, targetEntity = AssetFile.class)
    @JoinColumn(name = "image_id")
    private AssetFile image;

    @EqualsAndHashCode.Exclude
    @OneToMany(targetEntity = User.class, mappedBy = "company")
    @JsonIgnore
    private Set<User> users;

}
