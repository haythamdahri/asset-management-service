package org.management.asset.facades;

import org.springframework.security.core.Authentication;

/**
 * @author Haytham DAHRI
 */
public interface IAuthenticationFacade {

    Authentication getAuthentication();

}
