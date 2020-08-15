package org.management.asset.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.management.asset.bo.Entity;
import org.management.asset.bo.Organization;
import org.management.asset.dao.EntityRepository;
import org.management.asset.dao.OrganizationRepository;
import org.management.asset.dto.EntityRequestDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.services.EntityService;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Haytham DAHRI
 */
@Service
public class EntityServiceImpl implements EntityService {

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public Entity saveEntity(Entity entity) {
        return this.entityRepository.save(entity);
    }

    @Override
    public Integer getEntitiesCounter() {
        return this.entityRepository.findAll().size();
    }

    @Override
    public Entity saveEntity(EntityRequestDTO entityRequest) {
        try {
            final boolean entityIdNotExists = StringUtils.isEmpty(entityRequest.getId()) ||
                    entityRequest.getId() == null ||
                    StringUtils.equals(entityRequest.getId(), "null") ||
                    StringUtils.equals(entityRequest.getId(), "undefined");
            Entity entity;
            // Check if an other entity with same name in the organization
            Query query = new Query();
            query.addCriteria(Criteria.where("id").ne(entityRequest.getId()));
            query.addCriteria(Criteria.where("organization.id").is(entityRequest.getOrganization()));
            query.addCriteria(Criteria.where("name").is(entityRequest.getName()));
            if( this.mongoOperations.exists(query, Entity.class) ) {
                throw new BusinessException(Constants.ENTITY_NAME_ALREADY_TAKEN);
            }
            if( entityIdNotExists ) {
                entity = new Entity(null, entityRequest.getName(), null, null);
            } else {
                entity = this.entityRepository.findById(entityRequest.getId()).orElseThrow(BusinessException::new);
                entity.setName(entityRequest.getName());
            }
            if( entityRequest.getOrganization() != null ) {
                entity.setOrganization(this.organizationRepository.findById(entityRequest.getOrganization()).orElseThrow(BusinessException::new));
            }
            // Save entity
            return this.entityRepository.save(entity);
        } catch(BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch(Exception ex) {
            throw new TechnicalException(ex.getMessage());
        }
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

    @Override
    public Page<Entity> getEntities(String name, int page, int size, String direction, String... sort) {
        if( name == null || StringUtils.isEmpty(name)) {
            return this.entityRepository.findAll(PageRequest.of(page, size, Sort.Direction.valueOf(direction), sort));
        }
        return this.entityRepository.findByNameContainingIgnoreCase(name, PageRequest.of(page, size, Sort.Direction.valueOf(direction), sort));
    }

    @Override
    public List<Entity> getOrganizationEntities(String organizationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("organization.id").is(organizationId));
        return this.mongoOperations.find(query, Entity.class);
    }
}
