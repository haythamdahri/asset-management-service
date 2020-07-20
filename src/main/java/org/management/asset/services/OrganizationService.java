package org.management.asset.services;

import org.management.asset.bo.Organization;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface OrganizationService {

    Organization saveOrganization(Organization organization);

    boolean deleteOrganization(String id);

    Organization getOrganization(String id);

    Organization getOrganizationByName(String name);

    List<Organization> getOrganizations();

}
