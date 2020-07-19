package org.management.asset.services;

import org.management.asset.bo.Group;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface GroupService {

    Group saveGroup(Group group);

    boolean deleteGroup(String id);

    Group getGroup(String id);

    Group getGroupByName(String name);

    List<Group> getGroups();

}
