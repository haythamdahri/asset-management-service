package org.management.asset.configuration;

import org.management.asset.bo.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

/**
 * @author Haytham DAHRI
 */
@Configuration
public class RepositoryConfig implements RepositoryRestConfigurer {

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.exposeIdsFor(Organization.class, Entity.class, Group.class, Language.class, Location.class, Role.class, User.class);
    }

}
