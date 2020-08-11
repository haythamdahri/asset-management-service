package org.management.asset.services.impl;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.management.asset.bo.AssetFile;
import org.management.asset.bo.Organization;
import org.management.asset.dao.OrganizationRepository;
import org.management.asset.dao.UserRepository;
import org.management.asset.dto.OrganizationRequestDTO;
import org.management.asset.dto.OrganizationResponseDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.helpers.OrganizationHelper;
import org.management.asset.services.OrganizationService;
import org.management.asset.utils.ApplicationUtils;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

/**
 * @author Haytham DAHRI
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationHelper organizationHelper;

    @Override
    public Organization saveOrganization(Organization organization) {
        return this.organizationRepository.save(organization);
    }

    @Override
    public Organization saveOrganization(OrganizationRequestDTO organizationRequest) {
        try {
            final boolean organizationIdNotExists = StringUtils.isEmpty(organizationRequest.getId()) ||
                    organizationRequest.getId() == null ||
                    StringUtils.equals(organizationRequest.getId(), "null") ||
                    StringUtils.equals(organizationRequest.getId(), "undefined");
            // Set ID to null if empty
            Organization organization = this.organizationRepository.findById(organizationRequest.getId()).orElse(new Organization());
            if ((organizationIdNotExists && this.organizationRepository.findByNameIgnoreCase(organizationRequest.getName()).isPresent()) ||
                    (!organizationIdNotExists && !StringUtils.equals(organizationRequest.getName(), organization.getName()) && this.organizationRepository.findByNameIgnoreCase(organizationRequest.getName()).isPresent())) {
                throw new BusinessException(Constants.ORGANIZATION_NAME_ALREADY_USER);
            }
            // Set organization data
            organization.setName(organizationRequest.getName());
            organization.setDescription(organizationRequest.getDescription());
            // Set image
            if (organizationIdNotExists || organizationRequest.isUpdateImage()) {
                this.setOrganizationImage(organizationRequest.getImage(), organization);
            }
            // Save and return organization
            return this.organizationRepository.save(organization);
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new TechnicalException(ex.getMessage());
        }
    }

    private void setOrganizationImage(MultipartFile file, Organization organization) {
        try {
            // Retrieve user
            if (file == null || file.isEmpty() || !Constants.IMAGE_CONTENT_TYPES.contains(file.getContentType())) {
                throw new BusinessException(Constants.INVALID_USER_IMAGE);
            } else {
                // Update user image file and link it with current user
                AssetFile image = new AssetFile();
                image.setName(FilenameUtils.removeExtension(file.getOriginalFilename()));
                image.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
                image.setFile(file.getBytes());
                image.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())).toString());
                organization.setImage(image);
            }
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @Override
    public boolean deleteOrganization(String id) {
        this.organizationRepository.deleteById(id);
        return !this.organizationRepository.findById(id).isPresent();
    }

    @Override
    public Organization getOrganization(String id) {
        return this.organizationRepository.findById(id).orElse(null);
    }

    @Override
    public Organization getOrganizationByName(String id) {
        return this.organizationRepository.findById(id).orElse(null);
    }

    @Override
    public AssetFile getOrganizationImage(String id) {
        return this.organizationRepository.findById(id).map(Organization::getImage).orElse(null);
    }

    @Override
    public List<Organization> getOrganizations() {
        return this.organizationRepository.findAll();
    }

    @Override
    public List<OrganizationResponseDTO> getCustomOrganizations() {
        return this.organizationRepository.findCustomOrganizations();
    }

    @Override
    public Page<Organization> getOrganizations(String search, int page, int size, String direction, String... sort) {
        if (StringUtils.isEmpty(search)) {
            return this.organizationRepository.findAll(PageRequest.of(page, size, Sort.Direction.valueOf(direction), sort));
        } else {
            return this.organizationRepository.findByNameContainingIgnoreCase(ApplicationUtils.escapeSpecialRegexChars(search.toLowerCase().trim()), PageRequest.of(page, size, Sort.Direction.valueOf(direction), sort));
        }
    }

    @Override
    public Long getOrganizationsCounter() {
        return this.organizationRepository.countOrganizations();
    }
}
