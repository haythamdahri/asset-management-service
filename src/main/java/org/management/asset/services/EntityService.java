package org.management.asset.services;

import org.management.asset.bo.Entity;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface EntityService {

    Entity saveEntity(Entity entity);

    boolean deleteEntity(String id);

    Entity getEntity(String id);

    Entity getEntityByName(String name);

    List<Entity> getEntities();

}
