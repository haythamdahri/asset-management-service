package org.management.asset.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.management.asset.bo.Group;
import org.management.asset.dao.GroupRepository;
import org.management.asset.dao.RoleRepository;
import org.management.asset.dto.GroupDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.mappers.GroupMapper;
import org.management.asset.services.GroupService;
import org.management.asset.utils.ApplicationUtils;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Haytham DAHRI
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public Group saveGroup(Group group) {
        return this.groupRepository.save(group);
    }

    @Override
    public Group saveGroup(GroupDTO groupDTO) {
        try {
            Group group = this.groupMapper.toModel(groupDTO);
            // Set ID to null if empty
            final boolean groupIdNotExists = StringUtils.isEmpty(groupDTO.getId()) ||
                    groupDTO.getId() == null ||
                    StringUtils.equals(groupDTO.getId(), "null") ||
                    StringUtils.equals(groupDTO.getId(), "undefined");
            groupDTO.setId(groupIdNotExists ? null : groupDTO.getId());
            // Check group Name
            if ((groupIdNotExists && this.groupRepository.findByNameIgnoreCase(groupDTO.getName()).isPresent()) || (
                    !groupIdNotExists && !StringUtils.equals(group.getName(), groupDTO.getName()) && this.groupRepository.findByNameIgnoreCase(groupDTO.getName()).isPresent())
            ) {
                throw new BusinessException(Constants.GROUP_NAME_ALREADY_TAKEN);
            }
            // Set group roles
            if (groupIdNotExists || groupDTO.isUpdatePermissions()) {
                group.setRoles(null);
                groupDTO.getRoles().forEach(roleId -> this.roleRepository.findById(roleId).ifPresent(group::addRole));
            } else {
                Optional<Group> localGroupOptional = this.groupRepository.findById(groupDTO.getId());
                if( localGroupOptional.isPresent() ) {
                    group.setRoles(localGroupOptional.get().getRoles());
                    group.setUsers(localGroupOptional.get().getUsers());
                }
            }
            // Save group
            return this.groupRepository.save(group);
        } catch(BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new TechnicalException(ex.getMessage());
        }
    }

    @Override
    public Integer getGroupsCounter() {
        return this.groupRepository.findAll().size();
    }

    @Override
    public boolean deleteGroup(String id) {
        this.groupRepository.deleteById(id);
        return !this.groupRepository.findById(id).isPresent();
    }

    @Override
    public Group getGroup(String id) {
        return this.groupRepository.findById(id).orElse(null);
    }

    @Override
    public Group getGroupByName(String name) {
        return this.groupRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<Group> getGroups() {
        return this.groupRepository.findAll();
    }

    @Override
    public Page<Group> getGroups(String name, int page, int size) {
        if (StringUtils.isEmpty(name)) {
            return this.groupRepository.findAll(PageRequest.of(page, size, Sort.Direction.DESC, "id"));
        } else {
            return this.groupRepository.findByNameContainingIgnoreCase(ApplicationUtils.escapeSpecialRegexChars(name.toLowerCase().trim()), PageRequest.of(page, size, Sort.Direction.DESC, "id"));
        }
    }
}
