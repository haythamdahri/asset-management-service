package org.management.asset.mappers;

import org.management.asset.bo.Group;
import org.management.asset.dto.GroupDTO;
import org.mapstruct.*;

/**
 * @author Haytham DAHRI
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GroupMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
            value = {
                    @Mapping(source = "source.id", target = "id"),
                    @Mapping(source = "source.name", target = "name"),
                    @Mapping(source = "source.description", target = "description")
            })
    Group toModel(GroupDTO source);

}
