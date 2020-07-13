package org.management.asset.services;

import org.management.asset.bo.Location;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface LocationService {

    Location saveLocation(Location location);

    boolean deleteLocation(Long id);

    Location getLocation(Long id);

    Location getLocation(String name);

    List<Location> getLocations();

}
