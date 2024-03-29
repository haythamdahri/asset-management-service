package org.management.asset.services.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.management.asset.bo.AssetFile;
import org.management.asset.bo.User;
import org.management.asset.dao.*;
import org.management.asset.dto.ProfileRequestDTO;
import org.management.asset.dto.UserDTO;
import org.management.asset.dto.UserRequestDTO;
import org.management.asset.exceptions.BusinessException;
import org.management.asset.exceptions.TechnicalException;
import org.management.asset.helpers.UserHelper;
import org.management.asset.mappers.UserMapper;
import org.management.asset.services.EmailService;
import org.management.asset.services.UserService;
import org.management.asset.utils.ApplicationUtils;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Haytham DAHRI
 */
@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EntityRepository entityRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private GroupRepository groupRepository;

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

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User saveUser(UserRequestDTO userRequest, String authenticatedUserEmail) {
        try {
            System.out.println(userRequest);
            // Set ID to null if empty
            final boolean userRequestIdNotExists = StringUtils.isEmpty(userRequest.getId()) ||
                    userRequest.getId() == null ||
                    StringUtils.equals(userRequest.getId(), "null") ||
                    StringUtils.equals(userRequest.getId(), "undefined");
            userRequest.setId(userRequestIdNotExists ? null : userRequest.getId());
            // MAP DTO to BO
            User user = this.userMapper.toModel(userRequest);
            User originalUser;
            if (userRequest.getId() != null) {
                originalUser = this.userRepository.findById(userRequest.getId()).orElse(new User());
            } else {
                originalUser = new User();
            }
            // Check email and employeeNumber changes; if changed verify unique value
            if (this.userRepository.findByIdNotAndEmail(userRequest.getId(), userRequest.getEmail()).isPresent()) {
                throw new BusinessException(Constants.EMAIL_ALREADY_USED);
            } else if (this.userRepository.findByIdNotAndEmployeeNumber(userRequest.getId(), userRequest.getEmployeeNumber()).isPresent()) {
                throw new BusinessException(Constants.EMPLOYEE_NUMBER_ALREADY_USED);
            }
            // Check if permissions will be changed
            if (userRequest.isUpdatePermissions() || userRequest.getId() == null) {
                this.setUserPermissions(userRequest.getRoles(), userRequest.getGroups(), user);
            } else {
                user.setGroups(originalUser.getGroups());
                user.setRoles(originalUser.getRoles());
            }
            // Set user data
            this.setUserData(userRequest, user, originalUser, this.userRepository.findByEmail(authenticatedUserEmail).orElse(null));
            // Set columns
            this.setUnfilledColumns(originalUser, user);
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

    private void setUserData(UserRequestDTO userRequest, User user, User originalUser, User authenticatedUser) throws IOException {
        // Set Location
        if (userRequest.getLocation() != null && !StringUtils.isEmpty(userRequest.getLocation())) {
            user.setLocation(this.locationRepository.findById(userRequest.getLocation()).orElse(null));
        } else {
            user.setLocation(originalUser.getLocation());
        }
        // Set current user country if user request is not created yet
        if (userRequest.getId() == null) {
            user.setCountry(authenticatedUser.getCountry());
        }
        // Set Entity
        if (userRequest.getEntity() != null && !StringUtils.isEmpty(userRequest.getEntity())) {
            user.setEntity(this.entityRepository.findById(userRequest.getEntity()).orElse(null));
        } else {
            user.setEntity(originalUser.getEntity());
        }
        // Set Language
        if (userRequest.getLanguage() != null && !StringUtils.isEmpty(userRequest.getLanguage())) {
            user.setLanguage(this.languageRepository.findById(userRequest.getLanguage()).orElse(null));
        } else {
            user.setLanguage(originalUser.getLanguage());
        }
        // Set Manager
        user.setManager(this.userRepository.findById(userRequest.getManager()).orElse(null));
        // Set Organization
        if (userRequest.getId() == null) {
            // Set current user organization if user is not created yet
            user.setOrganization(authenticatedUser.getOrganization());
        } else {
            if (userRequest.getOrganization() != null && !StringUtils.isEmpty(userRequest.getOrganization())) {
                user.setOrganization(this.organizationRepository.findById(userRequest.getOrganization()).orElse(null));
            } else {
                user.setOrganization(originalUser.getOrganization());
            }
        }
        // Set Password
        if (userRequest.isUpdatePassword() || userRequest.getId() == null) {
            user.setPassword(this.passwordEncoder.encode(userRequest.getPassword()));
        } else {
            user.setPassword(originalUser.getPassword());
        }
        // Set Image
        if (userRequest.isUpdateImage() || userRequest.getId() == null) {
            this.updateLocalUserImage(userRequest.getImage(), user);
        } else {
            user.setAvatar(originalUser.getAvatar());
        }
        // Set update time
        user.setUpdateDate(LocalDateTime.now());
    }

    private void setUnfilledColumns(User source, User target) {
        // Set unfilled columns
        target.setCreationDate((!StringUtils.isEmpty(source.getId()) && source.getId() != null && source.getCreationDate() != null) ? source.getCreationDate() : LocalDateTime.now());
        target.setLastLogin((!StringUtils.isEmpty(source.getId()) && source.getId() != null && source.getLastLogin() != null) ? source.getLastLogin() : LocalDateTime.now());
        target.setActivationDate((!StringUtils.isEmpty(source.getId()) && source.getId() != null && source.getActivationDate() != null) ? source.getActivationDate() : LocalDateTime.now());
        target.setToken(source.getToken());
        target.setDeletionDate(source.getDeletionDate());
    }

    /**
     * Convenient method to update or set user image
     *
     * @param file
     * @param user
     * @throws IOException
     */
    private void updateLocalUserImage(MultipartFile file, User user) throws IOException {
        try {
            AssetFile avatar = new AssetFile();
            // Check file correctness
            if (file != null && !file.isEmpty() && !Constants.IMAGE_CONTENT_TYPES.contains(file.getContentType())) {
                throw new BusinessException(Constants.INVALID_USER_IMAGE);
            }
            // Get user image file or create a default one
            if (file == null || file.isEmpty()) {
                Path path = Paths.get(new URL(Constants.USER_DEFAULT_IMAGE).getFile());
                avatar.setName(FilenameUtils.removeExtension(path.getFileName().toString()));
                avatar.setExtension(FilenameUtils.getExtension(path.getFileName().toString()));
                avatar.setFile(IOUtils.toByteArray(new URL(Constants.USER_DEFAULT_IMAGE).openConnection().getInputStream()));
                avatar.setMediaType(MediaType.IMAGE_PNG_VALUE);
            } else {
                // Update user image file and link it with current user
                avatar.setName(FilenameUtils.removeExtension(file.getOriginalFilename()));
                avatar.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
                avatar.setFile(file.getBytes());
                avatar.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())).toString());
            }
            // Set user avatar
            user.setAvatar(avatar);
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    @Override
    public User saveUser(ProfileRequestDTO profileRequest, String email) {
        try {
            // Retrieve user
            User user = this.userRepository.findByEmail(email).orElseThrow(BusinessException::new);
            // Set profile data
            this.setProfileData(profileRequest, user);
            // Save user
            return this.userRepository.save(user);
        } catch (BusinessException ex) {
            ex.printStackTrace();
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new TechnicalException(ex);
        }
    }

    private void setProfileData(ProfileRequestDTO profileRequest, User user) {
        user.setUsername(profileRequest.getUsername());
        user.setFirstName(profileRequest.getFirstName());
        user.setLastName(profileRequest.getLastName());
        user.setWebsite(profileRequest.getWebsite());
        user.setAddress(profileRequest.getAddress());
        user.setCity(profileRequest.getCity());
        user.setState(profileRequest.getState());
        user.setCountry(profileRequest.getCountry());
        user.setZip(profileRequest.getZip());
        user.setNotes(profileRequest.getNotes());
        user.setTitle(profileRequest.getTitle());
        user.setPhone(profileRequest.getPhone());
        // Set Location
        this.locationRepository.findById(profileRequest.getLocation()).ifPresent(user::setLocation);
        // Set Language
        this.languageRepository.findById(profileRequest.getLanguage()).ifPresent(user::setLanguage);
        // Set encrypted password
        if (profileRequest.isUpdatePassword()) {
            user.setPassword(this.passwordEncoder.encode(profileRequest.getPassword()));
        }
    }

    @Override
    public User updateUserImage(MultipartFile file, String email) {
        try {
            User user = this.userRepository.findByEmail(email).orElseThrow(BusinessException::new);
            // Retrieve user
            if (file == null || file.isEmpty() || !Constants.IMAGE_CONTENT_TYPES.contains(file.getContentType())) {
                throw new BusinessException(Constants.INVALID_USER_IMAGE);
            } else {
                // Update user image file and link it with current user
                AssetFile avatar = new AssetFile();
                avatar.setName(FilenameUtils.removeExtension(file.getOriginalFilename()));
                avatar.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
                avatar.setFile(file.getBytes());
                avatar.setMediaType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())).toString());
                user.setAvatar(avatar);
                user = this.userRepository.save(user);
            }
            // Return avatar
            return user;
        } catch (BusinessException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new TechnicalException(ex);
        }
    }

    /**
     * Set user Roles And Groups
     *
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
                user.addRole(this.roleRepository.findById(roleId).orElseThrow(BusinessException::new));
            }
        });
        // Retrieve groups
        this.userHelper.extractList(groups).forEach(groupId -> {
            if (!StringUtils.isEmpty(groupId)) {
                user.addGroup(this.groupRepository.findById(groupId).orElseThrow(BusinessException::new));
            }
        });
    }

    @Override
    public User getUser(String id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public AssetFile getUserAvatar(String id) {
        return this.userRepository.findById(id).map(User::getAvatar).orElse(null);
    }

    @Override
    public UserDTO getCustomUser(String id) {
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
    public boolean deleteUser(String id) {
        this.userRepository.deleteById(id);
        return !this.userRepository.findById(id).isPresent();
    }

    @Override
    public List<User> getUsers() {
        return this.userRepository.findAll();
    }


    @Override
    public List<User> getOrganizationUsers(String organizationId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("organization.$id").is(new ObjectId(organizationId)));
        return this.mongoOperations.find(query, User.class);
    }

    @Override
    public Long getUsersCounter() {
        return this.userRepository.countUsers();
    }

    @Override
    public List<UserDTO> getCustomUsers() {
        return this.userRepository.findCustomUsers();
    }

    @Override
    public Page<User> getUsers(String search, int page, int size, String direction, String... sort) {
        // Check if search is required
        if (StringUtils.isEmpty(search)) {
            return this.userRepository.findAll(PageRequest.of(page, size, Sort.Direction.valueOf(direction), sort));
        }
        return this.userRepository.findBySearch(ApplicationUtils.escapeSpecialRegexChars(search.toLowerCase().trim()), PageRequest.of(page, size, Sort.Direction.valueOf(direction), sort));
    }

    @Override
    public Page<User> getUsers(String search, String excludedUserEmail, int page, int size) {
        // Check if search is required
        if (StringUtils.isEmpty(search)) {
            return this.userRepository.findAllWithUserExclusion(excludedUserEmail, PageRequest.of(page, size, Sort.Direction.DESC, "id"));
        }
        return this.userRepository.findBySearch(ApplicationUtils.escapeSpecialRegexChars(search.toLowerCase().trim()), excludedUserEmail, PageRequest.of(page, size, Sort.Direction.DESC, "id"));
    }

    @Override
    public Boolean requestUserPasswordReset(String email) {
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

    @Override
    public boolean checkTokenValidity(String token) {
        // Check token validity
        User user = this.userRepository.findByToken(token).orElse(null);
        if (user == null) {
            throw new BusinessException(Constants.INVALID_TOKEN);
        } else if (!user.checkTokenValidity()) {
            throw new BusinessException(Constants.EXPIRED_TOKEN);
        }
        return true;
    }

    @Override
    public User resetUserPassword(String token, String password) {
        // Retrieve user
        User user = this.userRepository.findByToken(token).orElse(null);
        // Check token validity and active user
        if (user == null) {
            throw new BusinessException(Constants.INVALID_TOKEN);
        } else if (!user.isActive()) {
            throw new BusinessException(Constants.ACCOUNT_NOT_ACTIVE);
        } else if (!user.checkTokenValidity()) {
            throw new BusinessException(Constants.EXPIRED_TOKEN);
        }
        // Update user password
        user.setPassword(this.passwordEncoder.encode(password));
        user.setToken(null);
        user.setExpiryDate(null);
        user.setUpdateDate(LocalDateTime.now());
        // Save user
        user = this.userRepository.save(user);
        // Send email
        this.emailService.sendResetPasswordCompleteEmail(user.getEmail(), "Mot de passe est changé");
        // Return user
        return user;
    }

}
