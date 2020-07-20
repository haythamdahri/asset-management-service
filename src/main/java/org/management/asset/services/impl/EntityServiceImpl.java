package org.management.asset.services.impl;

import org.management.asset.bo.Entity;
import org.management.asset.dao.EntityRepository;
import org.management.asset.services.EntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Service
public class EntityServiceImpl implements EntityService {

    @Autowired
    private EntityRepository entityRepository;

    @Override
    public Entity saveEntity(Entity entity) {
        return this.entityRepository.save(entity);
    }

    @Override
    public boolean deleteEntity(String id) {
        this.entityRepository.deleteById(id);
        return !this.entityRepository.findById(id).isPresent();
    }

    @Override
    public Entity getEntity(String id) {
        return this.entityRepository.findById(id).orElse(null);
    }

    @Override
    public Entity getEntityByName(String name) {
        return this.entityRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<Entity> getEntities() {
        return this.entityRepository.findAll();
    }
}
