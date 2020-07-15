package org.management.asset.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location implements Serializable {

    private static final long serialVersionUID = -7696425062436068517L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User manager;

    @Column(name = "address1")
    private String address1;

    @Column(name = "address2")
    private String address2;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "country")
    private String country;

    @Column(name = "zip")
    private String zip;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "asset_id")
    private AssetFile image;

}
