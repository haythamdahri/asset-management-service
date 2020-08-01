package org.management.asset.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.management.asset.bo.Asset;
import org.management.asset.bo.AssetFile;
import org.management.asset.bo.Location;
import org.management.asset.dao.LocationRepository;
import org.management.asset.dto.LocationRequestDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.services.LocationService;
import org.management.asset.utils.ApplicationUtils;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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
    public Location saveLocation(LocationRequestDTO locationRequest) {
        try {
            // Set ID to null if empty
            final boolean locationRequestIdNotExists = StringUtils.isEmpty(locationRequest.getId()) ||
                    locationRequest.getId() == null ||
                    StringUtils.equals(locationRequest.getId(), "null") ||
                    StringUtils.equals(locationRequest.getId(), "undefined");
            locationRequest.setId(locationRequestIdNotExists ? null : locationRequest.getId());
            Location location;
            if (locationRequestIdNotExists) {
                location = new Location();
            } else {
                location = this.locationRepository.findById(locationRequest.getId()).orElseThrow(BusinessException::new);
            }
            // Set data
            location.setName(locationRequest.getName());
            location.setAddress1(locationRequest.getAddress1());
            location.setAddress2(locationRequest.getAddress2());
            location.setCity(locationRequest.getCity());
            location.setZip(locationRequest.getZip());
            location.setState(locationRequest.getState());
            location.setCountry(locationRequest.getCountry());
            // Set location image
            if( locationRequestIdNotExists || locationRequest.isUpdateImage() ) {
                this.setLocationImage(locationRequest.getImage(), location);
            }
            // Save location
            return this.locationRepository.save(location);
        } catch(BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch(Exception ex) {
            throw new TechnicalException(ex.getMessage());
        }
    }

    /**
     * Set location image
     * @param file
     * @param location
     */
    private void setLocationImage(MultipartFile file, Location location) throws IOException {
        if (file == null || file.isEmpty() || !Constants.IMAGE_CONTENT_TYPES.contains(file.getContentType())) {
            throw new BusinessException(Constants.INVALID_LOCATION_IMAGE);
        } else {
            // Update asset image file and link it with current user
            AssetFile image = new AssetFile();
            image.setName(FilenameUtils.removeExtension(file.getOriginalFilename()));
            image.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
            image.setFile(file.getBytes());
            image.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())).toString());
            location.setImage(image);
        }
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

    @Override
    public Page<Location> getLocations(String search, int page, int size) {
        if( search == null || StringUtils.isEmpty(search) ) {
            return this.locationRepository.findAll(PageRequest.of(page, size, Sort.Direction.ASC, "id"));
        }
        return this.locationRepository.searchLocation(ApplicationUtils.escapeSpecialRegexChars(search.trim()), PageRequest.of(page, size, Sort.Direction.ASC, "id"));
    }
}
