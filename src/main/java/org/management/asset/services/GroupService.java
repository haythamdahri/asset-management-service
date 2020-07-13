package org.management.asset.services;

import org.management.asset.bo.Group;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface GroupService {

    Group saveGroup(Group group);

    boolean deleteGroup(Long id);

    Group getGroup(Long id);

    Group getGroup(String name);

    List<Group> getGroups();

}
