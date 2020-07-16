package org.management.asset.controllers;

import org.management.asset.bo.Location;
import org.management.asset.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping(path = "/")
    public ResponseEntity<List<Location>> listLocations() {
        return ResponseEntity.ok(this.locationService.getLocations());
    }

}
