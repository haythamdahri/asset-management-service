package org.management.asset.services;

import org.management.asset.bo.Location;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface LocationService {

    Location saveLocation(Location location);

    boolean deleteLocation(String id);

    Location getLocation(String id);

    Location getLocationByName(String name);

    List<Location> getLocations();

}
