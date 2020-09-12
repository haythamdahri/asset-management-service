package org.management.asset.controllers;

import org.management.asset.bo.Asset;
import org.management.asset.bo.AssetFile;
import org.management.asset.bo.Location;
import org.management.asset.dto.LocationRequestDTO;
import org.management.asset.services.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(path = "/counter")
    public ResponseEntity<Long> retrieveLocationsCounter() {
        return ResponseEntity.ok(this.locationService.getLocationsCounter());
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_LOCATIONS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/page")
    public ResponseEntity<Page<Location>> getLocationsPage(@RequestParam(value = "search", required = false, defaultValue = "") String search, @RequestParam(value = "page", required = false, defaultValue = "0") int page, @RequestParam(value = "size", required = false, defaultValue = "${page.default-size}") int size, @RequestParam(name = "sort", defaultValue = "id") String[] sort, @RequestParam(name = "direction", defaultValue = "DESC") String direction) {
        return ResponseEntity.ok(this.locationService.getLocations(search, page, size, direction, sort));
    }

    @GetMapping(path = "/{id}/image/file")
    public ResponseEntity<byte[]> getLocationImageFile(@PathVariable(name = "id") String id) {
        // Retrieve AssetFile
        Location location = this.locationService.getLocation(id);
        // Check file existence
        if (location != null && location.getImage() != null) {
            AssetFile image = location.getImage();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf(image.getMediaType()));
            return ResponseEntity.status(HttpStatus.OK).headers(headers).body(image.getFile());
        }
        // Return 404 not found
        return ResponseEntity.notFound().build();
    }

    @PostMapping(path = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Location> saveLocation(@ModelAttribute LocationRequestDTO locationRequest) {
        return ResponseEntity.ok(this.locationService.saveLocation(locationRequest));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_LOCATIONS_VIEW') or hasRole('ROLE_SUPER_USER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Location> getLocation(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(this.locationService.getLocation(id));
    }

    @PreAuthorize(value = "hasRole('ROLE_ADMIN') or hasRole('ROLE_LOCATIONS_DELETE') or hasRole('ROLE_SUPER_USER')")
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable(name = "id") String id) {
        this.locationService.deleteLocation(id);
        return ResponseEntity.ok().build();
    }

}
