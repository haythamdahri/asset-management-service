package org.management.asset.services;

import org.management.asset.bo.AssetFile;
import org.management.asset.bo.User;
import org.management.asset.dto.UserDTO;
import org.management.asset.dto.UserRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author Haytham DAHRI
 */
public interface UserService {

    User saveUser(User user);

    User saveUser(UserRequestDTO userRequest);

    User getUser(Long id);

    UserDTO getCustomUser(Long id);

    User getUserByToken(String token);

    User getActiveUser(String email);

    User getUserByEmail(String email);

    User getUserByUsername(String username);

    boolean deleteUser(Long id);

    List<User> getUsers();

    List<UserDTO> getCustomUsers();

    Page<User> getUsers(String search, String excludedUserEmail, int page, int size);

    User updateUserImage(MultipartFile file, String email, User user) throws IOException;

    boolean checkTokenValidity(String token);

    Boolean requestUserPasswordReset(String email);

    User resetUserPassword(String token, String password);

}
