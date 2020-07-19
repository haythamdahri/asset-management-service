package org.management.asset.services;


import org.management.asset.bo.Role;
import org.management.asset.bo.RoleType;

import java.util.List;

/**
 * @author Haytam DAHRI
 */
public interface RoleService {

    Role saveRole(Role role);

    Role getRole(String id);

    Role getRole(RoleType roleType);

    boolean deleteRole(String id);

    List<Role> getRoles();

}
