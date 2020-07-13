package org.management.asset.services.impl;

import org.management.asset.bo.Group;
import org.management.asset.dao.GroupRepository;
import org.management.asset.services.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Override
    public Group saveGroup(Group group) {
        return this.groupRepository.save(group);
    }

    @Override
    public boolean deleteGroup(Long id) {
        this.groupRepository.deleteById(id);
        return !this.groupRepository.findById(id).isPresent();
    }

    @Override
    public Group getGroup(Long id) {
        return this.groupRepository.findById(id).orElse(null);
    }

    @Override
    public Group getGroup(String name) {
        return this.groupRepository.findByNameIgnoreCase(name).orElse(null);
    }

    @Override
    public List<Group> getGroups() {
        return this.groupRepository.findAll();
    }
}
