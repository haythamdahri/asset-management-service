package org.management.asset.configuration;

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
import java.util.HashSet;
import java.util.stream.Stream;

/**
 * @author Haytham DAHRI
 */
@Configuration
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

//    @EventListener(value = {ApplicationReadyEvent.class})
//    @Transactional
    public void runApplication() throws IOException {
        Group superAdmins = null;
        // Add roles to the system
        if (this.roleService.getRoles().isEmpty()) {
            Stream.of(RoleType.values()).forEach(role -> {
                this.roleService.saveRole(new Role(null, role, null, null));
            });
        }
        // Add SuperAdmin Group
        if (this.groupService.getGroups().isEmpty()) {
            superAdmins = this.groupService.saveGroup(
                    new Group(null, "Super Admins", new HashSet<>(this.roleService.getRoles()), null));
        }
        // Add Demo User For Dev
        if (this.userService.getUsers().isEmpty()) {
            byte[] bytes = Files.readAllBytes(Paths.get("/home/haytham/Downloads/Database.png"));
            AssetFile companyImage = this.assetFileService.saveAssetFile(
                    new AssetFile(null, "Database.png", "png", MediaType.IMAGE_PNG_VALUE, bytes, null));
            AssetFile userAvatar = this.assetFileService.saveAssetFile(
                    new AssetFile(null, "Database.png", "png", MediaType.IMAGE_PNG_VALUE, bytes, null));
            Company company = this.companyService.saveCompany(new Company(null, "ACER", companyImage, null));
            Language language = this.languageService.saveLanguage(new Language(null, "Francais"));
            Department department = this.departmentService.saveDepartment(
                    new Department(null, "Système d'information", null));
            Location location = this.locationService.saveLocation(new Location(null, "Rabat, Morocco"));
            Country country = new Country(null, "Morocco");
            User user = this.userService.saveUser(new User(null, "Haytham", "Dahri", "haythamdahri", this.passwordEncoder.encode("toortoor"), "haytham.dahri@gmail.com",
                    company, language, "EMP56210", "Developer", null, null, department, location, "0689855298", "google.com",
                    "Developer", "Rabat", "Rabat", "Rabat-Kénitra", country, "10010", true, "", LocalDateTime.now().minusDays(5L),
                    LocalDateTime.now().minusHours(12L), null, LocalDateTime.now().minusMinutes(50L), null, LocalDateTime.now().minusMinutes(50L), userAvatar, null, null));
            superAdmins.addUser(user);
            this.roleService.getRoles().forEach(role -> {
                user.addRole(role);
            });
            this.userService.saveUser(user);
        }
    }

}
