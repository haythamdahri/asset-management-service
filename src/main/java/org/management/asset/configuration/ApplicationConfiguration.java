package org.management.asset.configuration;

import lombok.extern.log4j.Log4j2;
import org.management.asset.bo.*;
import org.management.asset.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * @author Haytham DAHRI
 */
@Configuration
@Log4j2
public class ApplicationConfiguration {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private AssetFileService assetFileService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @EventListener(value = {ApplicationReadyEvent.class})
    @Transactional
    public void runApplication() throws IOException {
        Group superAdmins = null;
        Group basicUsers = null;
        // Add roles to the system
        if (this.roleService.getRoles().isEmpty()) {
            Stream.of(RoleType.values()).forEach(role -> {
                this.roleService.saveRole(new Role(null, role, null, null));
            });
        }
        // Add SuperAdmin Group
        if (this.groupService.getGroups().isEmpty()) {
            superAdmins = this.groupService.saveGroup(
                    new Group(null, "Super Admins", this.roleService.getRoles(), null));
            basicUsers = this.groupService.saveGroup(
                    new Group(null, "Basic Users", Arrays.asList(this.roleService.getRole(RoleType.ROLE_USERS_VIEW)), null));
        }
        if (this.userService.getUsers().isEmpty()) {
            // Add ADMIN User For Dev
            byte[] bytes = Files.readAllBytes(Paths.get("/home/haytham/Downloads/profile.jpg"));
            AssetFile companyImage = this.assetFileService.saveAssetFile(
                    new AssetFile(null, "Database.png", "png", MediaType.IMAGE_PNG_VALUE, bytes, null));
            AssetFile userAvatar = this.assetFileService.saveAssetFile(
                    new AssetFile(null, "Database.png", "png", MediaType.IMAGE_PNG_VALUE, bytes, null));
            Company company = this.companyService.saveCompany(new Company(null, "ACER", companyImage, null));
            Language language = this.languageService.saveLanguage(new Language(null, "Francais"));
            Department department = this.departmentService.saveDepartment(
                    new Department(null, "Système d'information", null));
            AssetFile locationAssetFile = this.assetFileService.saveAssetFile(
                    new AssetFile(null, "Database.png", "png", MediaType.IMAGE_PNG_VALUE, bytes, null));
            Location location = this.locationService.saveLocation(this.locationService.saveLocation(new Location(null, "Rabat, Morocco", null, "Address 2", "Address 2"
                    , "Rabat", "Rabat-Kénitra", "Morocco", "10010", locationAssetFile)));
            User user = new User();
            user.setFirstName("Haytham");
            user.setLastName("Dahri");
            user.setUsername("haythamdahri");
            user.setPassword(this.passwordEncoder.encode("toortoor"));
            user.setEmail("haytham.dahri@gmail.com");
            user.setCompany(company);
            user.setLanguage(language);
            user.setEmployeeNumber("EMP56210");
            user.setTitle("Mr");
            user.setDepartment(department);
            user.setLocation(location);
            user.setPhone("0689855298");
            user.setWebsite("https://www.google.com");
            user.setJobTitle("Developer");
            user.setCity("Rabat");
            user.setAddress("Address Rabat");
            user.setState("Rabat-Kénitra");
            user.setCountry("Morocco");
            user.setZip("10010");
            user.setActive(true);
            user.setLocation(location);
            user.setCreationDate(LocalDateTime.now().minusDays(5L));
            user.setActivationDate(LocalDateTime.now().minusHours(12L));
            user.setLastLogin(LocalDateTime.now().minusMinutes(50L));
            user.setAvatar(userAvatar);
            user.setNotes("");
            // Assign user all roles & add it to SUPERADMINS
            assert superAdmins != null;
            superAdmins.addUser(user);
            this.roleService.getRoles().forEach(user::addRole);
            this.userService.saveUser(user);
            // ========================= Add Basic User For Dev =========================
            AssetFile newUserAvatar = this.assetFileService.saveAssetFile(
                    new AssetFile(null, "Database.png", "png", MediaType.IMAGE_PNG_VALUE, bytes, null));
            User basicUser = new User();
            basicUser.setFirstName("Basic");
            basicUser.setLastName("User");
            basicUser.setUsername("basic");
            basicUser.setPassword(this.passwordEncoder.encode("toortoor"));
            basicUser.setEmail("basic.user@gmail.com");
            basicUser.setCompany(company);
            basicUser.setLanguage(language);
            basicUser.setEmployeeNumber("EMP5000");
            basicUser.setTitle("Mr");
            basicUser.setManager(user);
            basicUser.setDepartment(department);
            basicUser.setLocation(location);
            basicUser.setPhone("0600223366");
            basicUser.setWebsite("https://www.google.com");
            basicUser.setJobTitle("Analyst");
            basicUser.setCity("Tanger");
            basicUser.setAddress("Address Tanger");
            basicUser.setState("Tanger");
            basicUser.setCountry("Morocco");
            basicUser.setZip("653000");
            basicUser.setActive(true);
            basicUser.setLocation(location);
            basicUser.setCreationDate(LocalDateTime.now().minusDays(5L));
            basicUser.setActivationDate(LocalDateTime.now().minusHours(12L));
            basicUser.setLastLogin(LocalDateTime.now().minusMinutes(50L));
            basicUser.setAvatar(newUserAvatar);
            basicUser.setNotes("");
            basicUser.addRole(this.roleService.getRole(RoleType.ROLE_USERS_VIEW));
            basicUser.addGroup(basicUsers);
            this.userService.saveUser(basicUser);
        }
        // Logging Message
        log.info("SYSTEM HAS BEEN INITIALIZED SUCCESSFULLY");
    }

}
