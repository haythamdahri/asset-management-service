package org.management.asset.services;

import org.management.asset.bo.AssetFile;
import org.management.asset.bo.Organization;
import org.management.asset.bo.Process;
import org.management.asset.dto.OrganizationRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface OrganizationService {

    Organization saveOrganization(Organization organization);

    Organization saveOrganization(OrganizationRequestDTO organizationRequest);

    boolean deleteOrganization(String id);

    Organization getOrganization(String id);

    AssetFile getOrganizationImage(String id);

    Organization getOrganizationByName(String name);

    List<Organization> getOrganizations();

    Page<Organization> getOrganizations(String search, int page, int size);

    Long getOrganizationsCounter();

}
