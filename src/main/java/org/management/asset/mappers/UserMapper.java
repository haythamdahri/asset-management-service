package org.management.asset.mappers;

import org.management.asset.bo.User;
import org.management.asset.dto.UserRequestDTO;
import org.mapstruct.*;

/**
 * @author Haytham DAHRI
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mappings(
            value = {
                    @Mapping(source = "source.id", target = "id"),
                    @Mapping(source = "source.username", target = "username"),
                    @Mapping(source = "source.firstName", target = "firstName"),
                    @Mapping(source = "source.lastName", target = "lastName"),
                    @Mapping(source = "source.email", target = "email"),
                    @Mapping(source = "source.active", target = "active"),
                    @Mapping(source = "source.employeeNumber", target = "employeeNumber"),
                    @Mapping(source = "source.website", target = "website"),
                    @Mapping(source = "source.address", target = "address"),
                    @Mapping(source = "source.city", target = "city"),
                    @Mapping(source = "source.state", target = "state"),
                    @Mapping(source = "source.country", target = "country"),
                    @Mapping(source = "source.zip", target = "zip"),
                    @Mapping(source = "source.notes", target = "notes"),
                    @Mapping(source = "source.title", target = "title"),
                    @Mapping(source = "source.phone", target = "phone"),
                    @Mapping(source = "source.jobTitle", target = "jobTitle")
            })
    User toModel(UserRequestDTO source);

}
