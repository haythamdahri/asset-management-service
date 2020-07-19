package org.management.asset.services.impl;

import org.management.asset.bo.Location;
import org.management.asset.dao.LocationRepository;
import org.management.asset.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Service
public class LocationServiceImpl implements LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public Location saveLocation(Location location) {
        return this.locationRepository.save(location);
    }

    @Override
    public boolean deleteLocation(String id) {
        this.locationRepository.deleteById(id);
        return !this.locationRepository.findById(id).isPresent();
    }

    @Override
    public Location getLocation(String id) {
        return this.locationRepository.findById(id).orElse(null);
    }

    @Override
    public Location getLocationByName(String name) {
        return this.locationRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<Location> getLocations() {
        return this.locationRepository.findAll();
    }
}
