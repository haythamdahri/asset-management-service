package org.management.asset.services.impl;

import org.management.asset.bo.AssetFile;
import org.management.asset.bo.User;
import org.management.asset.dao.*;
import org.management.asset.dto.UserDTO;
import org.management.asset.dto.UserRequestDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.helpers.UserHelper;
import org.management.asset.mappers.UserMapper;
import org.management.asset.services.AssetFileService;
import org.management.asset.services.EmailService;
import org.management.asset.services.UserService;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author Haytham DAHRI
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private AssetFileService assetFileService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserHelper userHelper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${token.expiration}")
    private Long tokenExpiration;

    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public User saveUser(UserRequestDTO userRequest) {
        try {
            // MAP DTO to BO
            User originalUser = this.userRepository.findById(Long.parseLong(userRequest.getId())).orElse(new User());
            User user = this.userMapper.toModel(userRequest);
            // Check email changes; if changed verify unique value
            if (originalUser.getId() != null && !user.getEmail().equalsIgnoreCase(userRequest.getEmail())) {
                throw new BusinessException(Constants.EMAIL_ALREADY_USED);
            }
            // Check if permissions will be changed
            if (userRequest.isUpdatePermissions()) {
                this.setUserPermissions(userRequest.getRoles(), userRequest.getGroups(), user);
            } else {
                user.setGroups(originalUser.getGroups());
                user.setRoles(originalUser.getRoles());
            }
            // Set Location
            if (userRequest.getLocation().equalsIgnoreCase("null") && StringUtils.isEmpty(userRequest.getLocation())) {
                user.setLocation(this.locationRepository.findById(Long.parseLong(userRequest.getLocation())).orElse(null));
            } else {
                user.setLocation(originalUser.getLocation());
            }
            // Set Department
            if (userRequest.getDepartment().equalsIgnoreCase("null") && StringUtils.isEmpty(userRequest.getDepartment())) {
                user.setDepartment(this.departmentRepository.findById(Long.parseLong(userRequest.getDepartment())).orElse(null));
            } else {
                user.setDepartment(originalUser.getDepartment());
            }
            // Set Language
            if (userRequest.getLanguage().equalsIgnoreCase("null") && StringUtils.isEmpty(userRequest.getLanguage())) {
                user.setLanguage(this.languageRepository.findById(Long.parseLong(userRequest.getLanguage())).orElse(null));
            } else {
                user.setLanguage(originalUser.getLanguage());
            }
            // Set Manager
            user.setManager(this.userRepository.findById(Long.parseLong(userRequest.getManager())).orElse(null));
            // Set Company
            if (userRequest.getCompany().equalsIgnoreCase("null") && StringUtils.isEmpty(userRequest.getCompany())) {
                user.setCompany(this.companyRepository.findById(Long.parseLong(userRequest.getCompany())).orElse(null));
            } else {
                user.setCompany(originalUser.getCompany());
            }
            // Set Password
            if (!StringUtils.isEmpty(userRequest.getPassword()) || userRequest.getId() == null) {
                user.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
            } else {
                user.setPassword(originalUser.getPassword());
            }
            // Set Image
            if (userRequest.isUpdateImage()) {
                this.updateUserImage(userRequest.getImage(), userRequest.getEmail(), user);
            } else {
                user.setAvatar(originalUser.getAvatar());
            }
            // Set update time
            user.setUpdateDate(LocalDateTime.now());
            // Set unfilled columns
            user.setCreationDate(originalUser.getCreationDate());
            user.setLastLogin(originalUser.getLastLogin());
            user.setActivationDate(originalUser.getActivationDate());
            user.setJobTitle(originalUser.getJobTitle());
            user.setToken(originalUser.getToken());
            user.setDeletionDate(originalUser.getDeletionDate());
            user.setSubordinates(originalUser.getSubordinates());
            // Save User
            return this.userRepository.save(user);
        } catch (BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex);
        }
    }

    @Override
    public User updateUserImage(MultipartFile file, String email,User user) throws IOException {
        try {
            // Retrieve user
            if (file == null || file.isEmpty() || !Constants.IMAGE_CONTENT_TYPES.contains(file.getContentType())) {
                throw new BusinessException(Constants.INVALID_USER_IMAGE);
            } else {
                // Update user image file and link it with current user
                AssetFile assetFile = this.assetFileService.saveAssetFile(file, user.getAvatar());
                user.setAvatar(assetFile);
            }
            // Return User
            return user;
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Set user Roles And Groups
     * @param roles
     * @param groups
     * @param user
     */
    private void setUserPermissions(String roles, String groups, User user) {
        user.setRoles(null);
        user.setGroups(null);
        // Retrieve Roles
        this.userHelper.extractList(roles).forEach(roleId -> {
            if (!StringUtils.isEmpty(roleId)) {
                user.addRole(this.roleRepository.findById(Long.parseLong(roleId)).orElseThrow(BusinessException::new));
            }
        });
        // Retrieve groups
        this.userHelper.extractList(groups).forEach(groupId -> {
            if (!StringUtils.isEmpty(groupId)) {
                user.addGroup(this.groupRepository.findById(Long.parseLong(groupId)).orElseThrow(BusinessException::new));
            }
        });
    }

    @Override
    public User getUser(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public UserDTO getCustomUser(Long id) {
        return this.userRepository.findCustomUserById(id).orElse(null);
    }

    @Override
    public User getUserByToken(String token) {
        return this.userRepository.findByToken(token).orElse(null);
    }

    @Override
    public User getActiveUser(String email) {
        return this.userRepository.findByEmailAndActiveIsTrue(email).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public boolean deleteUser(Long id) {
        this.userRepository.deleteById(id);
        return !this.userRepository.findById(id).isPresent();
    }

    @Override
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public List<UserDTO> getCustomUsers() {
        return this.userRepository.findCustomUsers();
    }

    @Override
    public Page<User> getUsers(String search, String excludedUserEmail, int page, int size) {
        // Check if search is required
        if (StringUtils.isEmpty(search)) {
            return this.userRepository.findAllWithUserExclusion(PageRequest.of(page, size, Sort.Direction.ASC, "id"), excludedUserEmail);
        }
        return this.userRepository.findBySearch(PageRequest.of(page, size, Sort.Direction.ASC, "id"), search.toLowerCase().trim(), excludedUserEmail);
    }

    @Override
    public boolean requestUserPasswordReset(String email) {
        // Retrieve user
        User user = this.userRepository.findByEmail(email).orElse(null);
        if (user != null && user.isActive()) {
            // Generate token and expiry date
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setExpiryDate(LocalDateTime.now().plusSeconds(this.tokenExpiration));
            // Save user
            user = this.userRepository.save(user);
            // Send password reset email
            this.emailService.sendResetPasswordEmail(user.getToken(), user.getEmail(), "Réinitialisation mot de passe");
        } else if (user != null && !user.isActive()) {
            throw new BusinessException(Constants.ACCOUNT_NOT_ACTIVE);
        } else {
            throw new BusinessException(Constants.EMAIL_NOT_FOUND);
        }
        // Return true for successful operation
        return true;
    }

}
