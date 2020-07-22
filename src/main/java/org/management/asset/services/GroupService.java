package org.management.asset.services;

import org.management.asset.bo.Group;
import org.management.asset.dto.GroupDTO;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface GroupService {

    Group saveGroup(Group group);

    Group saveGroup(GroupDTO groupDTO);

    boolean deleteGroup(String id);

    Group getGroup(String id);

    Group getGroupByName(String name);

    List<Group> getGroups();

    Page<Group> getGroups(String name, int page, int size);

}
