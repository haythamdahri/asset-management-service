package org.management.asset.services;

import org.management.asset.bo.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface UserService {

    User saveUser(User user);

    User getUser(Long id);

    User getUserByToken(String token);

    User getActiveUser(String email);

    User getUserByEmail(String email);

    User getUserByUsername(String username);

    boolean deleteUser(Long id);

    List<User> getUsers();

    Page<User> getUsers(String search, String excludedUserEmail, int page, int size);

}
