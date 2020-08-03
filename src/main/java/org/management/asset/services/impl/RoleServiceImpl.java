package org.management.asset.services.impl;

import org.management.asset.bo.Role;
import org.management.asset.bo.RoleType;
import org.management.asset.dao.RoleRepository;
import org.management.asset.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role saveRole(Role role) {
        return this.roleRepository.save(role);
    }

    @Override
    public Role getRole(String id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    @Override
    public Integer getRolesCounter() {
        return this.roleRepository.findAll().size();
    }

    @Override
    public Role getRole(RoleType roleType) {
        return this.roleRepository.findByRoleName(roleType).orElse(null);
    }

    @Override
    public boolean deleteRole(String id) {
        this.roleRepository.deleteById(id);
        return true;
    }

    @Override
    public List<Role> getRoles() {
        return this.roleRepository.findAll();
    }
}
