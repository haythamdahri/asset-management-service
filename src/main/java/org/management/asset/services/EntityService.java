package org.management.asset.services;

import org.management.asset.bo.Entity;
import org.management.asset.bo.Organization;
import org.management.asset.dto.EntityRequestDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface EntityService {

    Entity saveEntity(Entity entity);

    Entity saveEntity(EntityRequestDTO entityRequest);

    boolean deleteEntity(String id);

    Entity getEntity(String id);

    Entity getEntityByName(String name);

    List<Entity> getEntities();

    List<Entity> getOrganizationEntities(String organizationId);

    Page<Entity> getEntities(String name, int page, int size);

}
