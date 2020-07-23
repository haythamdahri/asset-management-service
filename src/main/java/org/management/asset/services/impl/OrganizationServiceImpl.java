package org.management.asset.services.impl;

import org.management.asset.bo.Organization;
import org.management.asset.dao.OrganizationRepository;
import org.management.asset.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Override
    public Organization saveOrganization(Organization organization) {
        return this.organizationRepository.save(organization);
    }

    @Override
    public boolean deleteOrganization(String id) {
        this.organizationRepository.deleteById(id);
        return !this.organizationRepository.findById(id).isPresent();
    }

    @Override
    public Organization getOrganizationByName(String id) {
        return this.organizationRepository.findById(id).orElse(null);
    }

    @Override
    public Organization getOrganization(String name) {
        return this.organizationRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<Organization> getOrganizations() {
        return this.organizationRepository.findAll();
    }

    @Override
    public Long getOrganizationsCounter() {
        return this.organizationRepository.countOrganizations();
    }
}
