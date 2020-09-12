package org.management.asset.services;

import org.management.asset.bo.Location;
import org.management.asset.dto.LocationRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface LocationService {

    Location saveLocation(Location location);

    Location saveLocation(LocationRequestDTO locationRequest);

    boolean deleteLocation(String id);

    Location getLocation(String id);

    Location getLocationByName(String name);

    List<Location> getLocations();

    long getLocationsCounter();

    Page<Location> getLocations(String search, int page, int size, String direction, String... sort);

}
