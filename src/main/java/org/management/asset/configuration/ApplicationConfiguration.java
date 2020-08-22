package org.management.asset.configuration;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;
import org.management.asset.bo.*;
import org.management.asset.dao.SettingRepository;
import org.management.asset.services.*;
import org.management.asset.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Haytham DAHRI
 */
@Configuration
@Log4j2
@Profile("dev")
public class ApplicationConfiguration {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private LanguageService languageService;

    @Autowired
    private EntityService entityService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @EventListener(value = {ApplicationReadyEvent.class})
    public void runApplication() throws IOException {
        Group superAdmins = null;
        Group basicUsers = null;
        // Add roles to the system
        if (this.roleService.getRoles().isEmpty()) {
            Stream.of(RoleType.values()).forEach(role -> this.roleService.saveRole(new Role(null, role, role.name(), null)));
        }
        // Add SuperAdmin Group
        if (this.groupService.getGroups().isEmpty()) {
            superAdmins = this.groupService.saveGroup(
                    new Group(null, "Super Admins", "Utilisateurs Admins", new HashSet<>(this.roleService.getRoles()), null));
            basicUsers = this.groupService.saveGroup(
                    new Group(null, "Basic Users", "Utilisateurs Basiques", Stream.of(this.roleService.getRole(RoleType.ROLE_USERS_VIEW)).collect(Collectors.toCollection(HashSet::new)), null));
        } else {
            superAdmins = this.groupService.getGroupByName("Super Admins");
            basicUsers = this.groupService.getGroupByName("Basic Users");
        }
        if (this.userService.getUsers().isEmpty()) {
            // Add ADMIN User For Dev
            AssetFile avatar = new AssetFile("Database.png", "png", MediaType.IMAGE_PNG_VALUE, IOUtils.toByteArray(new URL(Constants.USER_DEFAULT_IMAGE).openConnection().getInputStream()), LocalDateTime.now(), LocalDateTime.now());
            AssetFile organizationAvatar = new AssetFile("Database.png", "png", MediaType.IMAGE_PNG_VALUE, IOUtils.toByteArray(new URL(Constants.ORGANIZATON_DEFAULT_IMAGE).openConnection().getInputStream()), LocalDateTime.now(), LocalDateTime.now());
            Organization acerOrganization = this.organizationService.saveOrganization(new Organization(null, "ACER", "Description", organizationAvatar));
            Organization dellOrganization = this.organizationService.saveOrganization(new Organization(null, "DELL", "Description", organizationAvatar));
            Language language = this.languageService.saveLanguage(new Language(null, "Francais"));
            Entity entity = this.entityService.saveEntity(
                    new Entity(null, "Système d'information", null, null));
            Location location = this.locationService.saveLocation(new Location(null, "Rabat, Morocco", "Address 2", "Address 2"
                    , "Rabat", "Rabat-Kénitra", "Morocco", "10010", avatar));
            User user = new User();
            user.setFirstName("Haytham");
            user.setLastName("Dahri");
            user.setUsername("haythamdahri");
            user.setPassword(this.passwordEncoder.encode("toortoor"));
            user.setOrganization(acerOrganization);
            user.setEmail("haytham.dahri@gmail.com");
            user.setLanguage(language);
            user.setEmployeeNumber("EMP56210");
            user.setTitle("Mr");
            user.setEntity(entity);
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
            user.setAvatar(avatar);
            user.setNotes("");
            user.setCreationDate(LocalDateTime.now());
            // Assign user all roles & add it to SUPERADMINS
            assert superAdmins != null;
            superAdmins.addUser(user);
            this.roleService.getRoles().forEach(user::addRole);
            this.userService.saveUser(user);
            // ========================= Add Basic User For Dev =========================
            User basicUser = new User();
            basicUser.setCreationDate(LocalDateTime.now());
            basicUser.setFirstName("Basic");
            basicUser.setLastName("User");
            basicUser.setUsername("basic");
            basicUser.setPassword(this.passwordEncoder.encode("toortoor"));
            basicUser.setEmail("basic.user@gmail.com");
            basicUser.setOrganization(acerOrganization);
            basicUser.setLanguage(language);
            basicUser.setEmployeeNumber("EMP5000");
            basicUser.setTitle("Mr");
            basicUser.setManager(user);
            basicUser.setEntity(entity);
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
            basicUser.setAvatar(avatar);
            basicUser.setNotes("");
            basicUser.addRole(this.roleService.getRole(RoleType.ROLE_USERS_VIEW));
            basicUser.addGroup(basicUsers);

            this.userService.saveUser(user);
            this.userService.saveUser(basicUser);

//             =============== Generate Users ===============
            for (int i = 0; i < 10; i++) {
                basicUser = new User();
                basicUser.setCreationDate(LocalDateTime.now());
                basicUser.setFirstName("Basic" + i);
                basicUser.setLastName("User" + i);
                basicUser.setUsername("basic" + i);
                basicUser.setPassword(this.passwordEncoder.encode("toortoor"));
                basicUser.setEmail("basic.user" + i + "@gmail.com");
                basicUser.setOrganization(dellOrganization);
                basicUser.setLanguage(language);
                basicUser.setEmployeeNumber("EMP5000" + i);
                basicUser.setTitle("Mr");
                basicUser.setManager(null);
                basicUser.setEntity(entity);
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
                basicUser.setAvatar(avatar);
                basicUser.setNotes("My Notes " + i);
                basicUser.addRole(this.roleService.getRole(RoleType.ROLE_USERS_VIEW));
                basicUser.addGroup(basicUsers);
                this.userService.saveUser(basicUser);
            }
        }
        // Create  new riskAnalysis for the asset
//        Asset asset = this.assetRepository.findAll().stream().findFirst().get();
//        if( asset.getRiskAnalyzes().isEmpty() ) {
//            RiskAnalysis riskAnalysis = new RiskAnalysis();
//            riskAnalysis.setStatus(true);
//            riskAnalysis.setProbability(2);
//            riskAnalysis.setFinancialImpact(2);
//            riskAnalysis.setOperationalImpact(2);
//            riskAnalysis.setReputationalImpact(2);
//            riskAnalysis.setIdentificationDate(LocalDateTime.now(ZoneId.of("UTC+1")));
//            if( asset.getRiskAnalyzes() == null ) {
//                asset.setRiskAnalyzes(new HashSet<>());
//            }
//            asset.setRiskAnalyzes(new HashSet<>(Collections.singletonList(riskAnalysis)));
//            this.assetRepository.save(asset);
//        }

        // Set Default Application Configuration
        if (this.settingRepository.findAll().isEmpty()) {
            Setting setting = new Setting();
            setting.setProbabilities(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).collect(Collectors.toList()));
            setting.setFinancialImpacts(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).collect(Collectors.toList()));
            setting.setOperationalImpacts(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).collect(Collectors.toList()));
            setting.setReputationalImpacts(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).collect(Collectors.toList()));
            setting.setTargetFinancialImpacts(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).collect(Collectors.toList()));
            setting.setTargetOperationalImpacts(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).collect(Collectors.toList()));
            setting.setTargetReputationalImpacts(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).collect(Collectors.toList()));
            setting.setTargetProbabilities(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).collect(Collectors.toList()));
            setting.setAcceptableResidualRisks(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).collect(Collectors.toList()));
            setting.setConfidentialities(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).collect(Collectors.toList()));
            setting.setAvailabilities(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).collect(Collectors.toList()));
            setting.setIntegrities(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).collect(Collectors.toList()));
            setting.setTraceabilities(Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10).collect(Collectors.toList()));
            setting.setIdentificationDate(LocalDateTime.now(ZoneId.of("UTC+1")));
            setting.setMaxAttemptsWithoutCaptcha(5);
            this.settingRepository.save(setting);
        }
        // Logging Message
        log.info("SYSTEM HAS BEEN INITIALIZED SUCCESSFULLY");
    }

}
