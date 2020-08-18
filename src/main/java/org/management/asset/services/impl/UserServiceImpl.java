package org.management.asset.services.impl;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FilenameUtils;
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
import org.springframework.core.io.ResourceLoader;
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

    @Autowired
    private ResourceLoader resourceLoader;

    private final byte[] bytes = new byte[]{-1, -40, -1, -32, 0, 16, 74, 70, 73, 70, 0, 1, 1, 1, 0, 72, 0, 72, 0, 0, -1, -37, 0, 67, 0, 5, 3, 4, 4, 4, 3, 5, 4, 4, 4, 5, 5, 5, 6, 7, 12, 8, 7, 7, 7, 7, 15, 11, 11, 9, 12, 17, 15, 18, 18, 17, 15, 17, 17, 19, 22, 28, 23, 19, 20, 26, 21, 17, 17, 24, 33, 24, 26, 29, 29, 31, 31, 31, 19, 23, 34, 36, 34, 30, 36, 28, 30, 31, 30, -1, -37, 0, 67, 1, 5, 5, 5, 7, 6, 7, 14, 8, 8, 14, 30, 20, 17, 20, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, -1, -62, 0, 17, 8, 0, -128, 0, -128, 3, 1, 34, 0, 2, 17, 1, 3, 17, 1, -1, -60, 0, 28, 0, 0, 2, 3, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 5, 3, 4, 6, 1, 0, 7, 8, -1, -60, 0, 26, 1, 0, 2, 3, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 4, 0, 1, 3, 5, 6, -1, -38, 0, 12, 3, 1, 0, 2, 16, 3, 16, 0, 0, 1, -6, -79, 9, 4, 34, 18, -112, -92, -116, -18, 73, -17, 122, -25, -110, -70, -8, -80, -99, -70, -120, -100, -122, -101, 93, -81, -25, -81, -87, 107, -106, -37, -34, -11, -115, 2, 2, 8, 68, 5, 33, -104, 29, -55, 125, -50, -36, -25, -53, 31, 124, -53, 6, -73, -82, 126, 115, 111, 22, 89, -23, 80, -18, -102, 73, -89, -67, -19, 49, 93, -48, -24, 73, 10, 35, -110, 83, -118, 75, -110, -108, 113, -35, 85, -7, -42, -81, -25, -72, 48, -13, 71, -101, -44, 98, -12, -121, -54, -116, -14, 91, -78, -56, 58, -35, 57, -69, 17, 96, -12, -121, 17, 73, 60, -112, 76, 85, 54, 99, 69, -124, 53, -119, 59, -14, -75, -110, -72, -81, 111, 54, -40, 80, 3, -47, 78, 59, 76, -12, -60, 9, 76, -118, -11, -102, 18, -42, 23, 38, -102, -68, -92, 53, -78, 14, -106, -102, 28, -80, -83, -127, 99, 56, -100, 55, 14, 122, 69, 86, 26, -116, 118, -54, -17, 46, 96, 74, 118, 38, 114, -102, 82, -89, 49, -84, -60, 75, -10, -119, 14, -43, 103, 106, 86, -118, -81, 67, -50, 53, -88, -65, -91, -123, -38, 86, -69, 68, -101, 87, -102, 117, -51, -11, 83, -111, -11, -98, 127, 99, -103, 117, 104, -83, -115, 77, 48, -76, -99, -21, 111, 111, -50, -64, -83, -6, 69, 51, 83, 18, -53, 94, -112, 110, -103, -36, -120, 88, -81, 103, -73, -16, -24, -44, -20, -34, -39, 24, -20, 13, -85, -117, 93, 69, 102, -56, -53, -36, -80, 60, 30, -47, 16, 105, -97, 121, -102, -47, 98, -4, -15, 105, 44, -80, -110, 118, -111, 78, 107, -1, 0, -1, -60, 0, 38, 16, 0, 2, 2, 1, 3, 4, 2, 3, 1, 1, 0, 0, 0, 0, 0, 0, 2, 3, 1, 4, 0, 5, 17, 18, 16, 19, 32, 34, 6, 33, 20, 49, 50, 35, 48, -1, -38, 0, 8, 1, 1, 0, 1, 5, 2, -15, -113, 13, 87, 83, -83, -90, -82, -41, -54, 44, -100, 35, -28, 119, -57, 52, -17, -112, 41, -60, 4, 38, 30, 113, -31, -82, 41, -42, 44, -43, -45, -20, 28, -106, -110, 99, 22, 80, 64, -49, -120, -68, -114, -105, -100, 120, 51, -35, -87, -119, -56, -36, -105, 121, 65, 41, -8, -62, 59, 117, -1, 0, -31, 29, 53, 90, -60, -5, -62, -53, -54, 117, -122, -37, -112, -45, 27, 117, 118, 95, 78, -63, -77, 75, 30, 21, 60, -29, 35, -90, -94, -128, 117, 109, 49, 81, 55, 109, 47, -73, 99, 79, 71, 111, 30, 34, 121, 78, -53, 123, 1, 115, 124, 91, 32, -29, -58, 58, 57, -94, -96, -79, 121, -123, -113, 22, 11, -107, 12, -19, 41, -111, -63, 113, -54, 10, 118, 86, -8, -119, -11, -16, -116, -114, -102, -109, -73, 121, 71, -35, -108, -55, -32, 73, -120, 84, 92, -52, 54, 125, 74, 119, 47, -66, 85, 127, 94, 17, -111, -116, 46, 11, 121, 111, 37, 49, -35, 98, -73, -114, 24, -66, 88, 37, -68, 46, 57, 21, -97, -69, 53, 126, -4, 99, 35, 53, 18, -29, 89, -97, -64, 79, 36, -84, -66, -74, -116, 111, -86, -10, -40, 43, 126, -44, 93, -57, 43, -18, 33, -45, -99, -23, -56, 116, -28, 78, -15, 25, 25, -87, 56, 102, -61, 7, -42, -95, -6, -57, -20, 62, -53, -5, 107, 14, 103, 30, -56, 82, 116, -19, -5, 21, -1, 0, -116, -116, 28, 15, -30, 50, -61, -62, -70, 97, -59, 102, -60, 73, -16, -74, -80, 72, 12, -25, 47, -13, 99, 70, 1, 27, -27, -78, -103, 20, 25, -120, -45, 61, -29, -96, -27, -28, -79, -44, -21, -35, 91, 40, -39, -80, -37, -83, 66, -72, 102, -108, -67, -14, -45, -73, 108, 58, 54, -105, 114, -59, 12, -74, 76, -80, 81, 37, -106, 23, 63, -103, 82, 8, 99, -96, -27, -9, -110, -44, 82, 20, 108, 83, -104, 112, 45, 4, -10, -34, 33, 74, -37, -79, 65, 0, 98, -110, 76, -106, 111, -120, 86, 19, -89, 112, 76, 108, 17, -57, 54, -51, -78, 51, 86, -113, 74, -28, 99, -102, 127, 105, -96, 35, 1, 28, 98, 114, -30, -95, -107, 116, -30, 23, 81, 41, 80, -59, -109, 108, -77, -77, 44, 92, -15, 2, -119, -120, -118, 75, -18, -73, 108, -37, 54, -62, -84, 54, 18, 26, 100, 9, 37, 112, -72, -56, -24, -45, 42, -18, 22, -105, 38, 79, 115, 40, -54, -1, 0, 32, -76, -21, 13, -80, -67, 57, 66, 41, -51, -77, 108, -37, 4, -92, 71, -70, 120, -78, 35, -49, 124, -10, -62, -125, -53, 72, 113, 17, -86, -28, 59, 79, 91, 113, 64, 35, -101, -25, 46, 114, -91, -118, -57, -1, -60, 0, 33, 17, 0, 2, 2, 1, 4, 3, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 17, 3, 4, 16, 18, 33, 19, 32, 49, 65, 81, -1, -38, 0, 8, 1, 3, 1, 1, 63, 1, -12, 68, 44, 103, -123, -86, 17, -19, -90, 110, 38, -29, -28, -114, -121, -113, 47, 82, 106, 104, -77, 6, 37, 68, 126, -123, 77, 118, 82, 0, 81, 23, 41, 30, -102, -106, -91, -104, -14, 50, 55, 37, -121, 95, -112, -3, -100, -53, -98, 77, 23, -74, -107, 43, 109, 85, -36, 31, 55, -57, -77, 68, -57, -52, -52, -59, 60, 125, -120, -1, 0, 103, 47, -28, 6, -90, 32, -66, 33, -42, -40, -88, -72, -72, -43, -116, 25, -103, -114, 72, -38, 107, -3, -117, -96, 39, -28, -45, -24, 85, 77, -68, 119, -95, -75, -62, -20, 126, -19, -116, -15, 55, 11, -83, 92, -55, -104, -79, -22, 23, 36, -49, -1, -60, 0, 31, 17, 0, 2, 2, 1, 5, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 0, 17, 3, 16, 18, 32, 33, 49, 19, 50, -1, -38, 0, 8, 1, 2, 1, 1, 63, 1, -32, -52, 22, 125, 22, -21, -98, 117, -35, 23, 20, 86, 30, 114, -52, 60, 48, 30, -26, 17, -23, -98, -16, 16, -90, -31, 80, 96, 89, 85, -48, -46, -12, 17, 120, 13, 4, 102, -37, 17, 91, 119, 80, 105, 87, 13, -17, -47, -65, 48, 13, -15, 19, 108, -72, 114, -127, 50, 101, 53, 75, 21, 59, -44, 80, -14, 92, 110, -59, 69, 83, 117, 21, 2, -114, -12, -1, -60, 0, 51, 16, 0, 2, 1, 2, 3, 5, 6, 5, 3, 5, 0, 0, 0, 0, 0, 0, 1, 2, 0, 3, 17, 18, 33, 49, 4, 50, 65, 81, 97, 16, 19, 32, 34, 48, 113, 35, 66, 82, -127, -111, 20, 67, -95, 98, 114, -63, -47, -16, -1, -38, 0, 8, 1, 1, 0, 6, 63, 2, -12, 85, -85, -30, 37, -73, 85, 120, -53, 81, -92, -108, -70, -17, 25, -26, 101, 113, -43, 102, 13, -91, 86, -109, 112, 107, -28, 96, 116, 32, -87, -48, -113, 82, -91, 87, 108, 77, 123, 1, -56, 76, -46, 98, 2, 1, -121, -51, 120, -37, 59, 126, -47, -56, -5, -6, -116, -41, -29, -40, -34, -48, -80, -5, 74, -107, 62, -77, -23, -41, 21, -10, -118, -35, -56, -95, -34, 82, -90, -83, 97, 113, -81, -8, -99, -35, 63, -119, -47, -94, 1, 91, 54, 76, 118, 81, 107, -12, -68, 106, 52, -74, 99, -114, -64, -97, 61, -44, -33, -34, 49, -38, 118, -127, 79, 71, 61, -40, -118, -127, 108, -96, -100, 61, 71, 63, 77, -18, -66, 96, -89, 9, -30, 38, 34, 73, -73, 51, 5, 42, 86, -74, -93, 18, -34, -41, -116, -20, 113, 59, 102, 77, -93, -30, 23, -54, 124, 64, 46, 50, -54, 110, -97, 71, 19, 75, 11, 5, -116, 85, -40, 123, 69, 33, 111, -17, -108, -27, 9, -6, -89, -9, 27, -52, -96, -12, 48, -16, 88, 86, 101, -81, 89, -128, 83, 31, -103, 118, -60, 126, -42, 19, 13, -13, 111, -32, 113, -20, 2, 2, 52, -47, 127, -33, -115, -101, -112, -105, -127, -72, 25, 113, 50, -50, 97, -48, 70, 127, -80, -127, 121, -52, 60, 56, -5, 76, 124, 52, 30, 59, 125, 80, -60, -10, -104, 101, -26, 30, 45, 20, 71, -85, -12, -28, 61, -29, 55, 91, 64, -93, 79, 5, -5, 69, 28, 89, -127, 126, -49, 99, 110, -50, -112, -79, -48, 64, 23, 120, -23, 48, 46, -118, 63, 50, -103, -26, 47, -31, 29, -115, 86, -95, -14, -120, -43, -101, 86, 48, 42, -26, 78, 64, 74, 116, -105, 121, 119, -49, 51, -40, 102, -66, 81, -87, -99, -21, 100, 78, -24, -28, 33, 2, 40, -58, -39, 117, -102, -33, -64, 59, -105, 43, 85, 14, 58, 103, -88, -97, -86, 111, 40, 3, -50, 57, 30, 83, 21, 75, -127, -14, -81, 40, 47, 108, 80, -19, 15, -70, -102, 71, 99, -60, -34, 94, 107, 100, -25, 3, -80, -75, 49, -70, 57, -11, -20, 3, -119, -107, 81, 52, 15, 105, -97, -127, 17, 114, 45, -58, 22, 96, 95, 103, -38, 115, 55, -49, 11, -58, -58, -128, 114, -126, -99, 45, 62, 99, -54, 46, -52, -103, 40, 25, -10, 110, -33, -34, 93, -73, 121, 75, 42, -73, -30, 98, 124, -67, -27, -88, -36, 127, 87, 25, -97, -122, -111, 107, -31, -66, -78, -63, -107, -41, -111, -122, -12, 48, 21, 54, -62, 68, -78, 0, -93, -92, -71, 0, -54, -88, 0, -51, 76, -89, 80, -127, 123, 89, -66, -46, -28, -84, 24, 92, -46, -90, 71, -45, -103, -98, 106, -83, 82, -36, -27, -106, 28, -8, 65, 80, -113, 34, -26, 58, -8, 48, -75, -27, -5, -22, -115, -8, -106, 3, -63, 87, 100, -3, -78, -40, -65, -17, -30, 12, 92, -27, 55, -57, -16, 70, 76, -106, -4, 70, 10, -92, 117, -114, -3, -14, -45, 66, 114, 1, 120, 79, 51, 51, 30, 102, 112, -72, -53, 47, 6, 93, -70, -119, -88, -101, -45, 18, -70, -3, -46, -13, -30, 43, 84, 7, 44, 66, 16, -21, 52, -20, 42, -68, 53, -76, -78, -49, -1, -60, 0, 39, 16, 1, 0, 2, 2, 1, 3, 2, 7, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 17, 33, 49, 65, 16, 81, 97, 113, -127, 32, -111, -95, -79, -63, -47, -16, -15, -31, -1, -38, 0, 8, 1, 1, 0, 1, 63, 33, 33, -48, -121, -62, 96, 44, -62, 91, 72, -128, 13, -29, -8, 17, -80, -15, -81, -15, 49, -20, 44, 63, -59, 13, 74, -38, 44, 78, -92, 58, 16, -24, 58, 51, 53, 103, -61, 61, 84, 2, -123, 46, -40, 51, -55, -15, 21, 104, 22, 113, 118, 122, -62, -74, -2, 81, 102, -66, 2, 16, -121, 65, -47, 64, -74, 90, -104, -37, 97, 42, -126, 80, -126, -14, -63, 21, 74, 108, 41, -26, 51, 77, -46, 15, -89, -64, 116, 33, 14, -95, 70, 30, -57, 66, 123, -45, 127, 121, 74, -82, -35, 115, 98, -9, 4, 16, 12, 46, -59, -20, -69, -104, 31, 20, -70, -117, 30, -58, 90, 115, -32, -121, 27, -108, -32, 83, 43, -55, -113, 119, 83, -95, 8, 124, 6, 102, -100, 44, 37, 112, -112, -54, -115, 91, -85, -5, 19, 13, 2, -61, 98, 90, 28, 99, -60, 28, -103, -104, 109, 20, 4, -69, 82, -36, -67, -42, -119, 97, 65, 78, -55, -120, 43, -121, -38, 28, 76, 95, 120, 50, -32, -63, -125, -48, 75, 51, -24, 27, 99, 8, -21, 86, -31, 16, -106, -101, 112, -53, -47, 38, 110, -120, -107, -74, -46, -78, -53, -125, 22, -25, -76, 26, 12, 45, -19, -30, 88, 43, 105, 125, 110, -100, 126, 15, -52, -71, 112, 96, -11, 9, 68, 28, 85, -17, -52, 119, -111, -103, -91, 14, -119, -54, 21, 1, -81, 18, 106, 43, -33, -3, 18, -110, 120, 56, 125, 87, -29, -34, 103, -8, -30, 56, 13, -65, -17, -22, 81, -46, 125, -34, -15, 112, -105, 6, 40, -25, -111, 102, 34, -73, -65, -38, 120, 24, 126, -112, 35, 56, -35, 7, -103, 105, -95, 25, 107, 68, -35, 85, 121, 123, 31, -41, 20, 125, -70, -128, 1, 119, -68, 61, 24, -9, -88, 41, 106, 6, 19, 85, 8, 66, 17, 116, -38, 29, -86, -101, -107, 120, -71, 102, 54, 18, 61, -105, 58, -108, 12, 36, -68, -74, 125, 74, -57, 72, -48, -9, -101, -105, -125, -122, -81, 66, 96, 10, 6, 124, 19, -69, -124, -82, 113, 8, 7, 49, 71, 42, -102, -83, 61, 99, -35, 12, 60, 76, -77, -106, 80, -63, -35, -90, 101, -36, -96, 33, -78, 81, -105, 83, -70, -18, -27, -98, 65, -11, 16, -47, 31, 85, -124, 83, 105, -12, -111, 74, 59, 15, -51, -19, 57, -36, 21, -37, -60, -69, 10, -115, -117, 96, -18, 38, -46, 108, -49, 68, -96, 27, 95, -92, 68, 97, -101, -67, 16, 19, -89, 73, -3, 92, -27, 106, 45, 112, 42, 20, 109, 122, -104, 66, 109, 8, 1, -128, 125, -89, -72, -22, 91, 107, -81, 7, -65, -102, 82, 66, -66, 3, -5, -106, 48, 45, -118, -25, -42, 18, -61, 42, -65, 126, 89, -98, 46, 4, 110, 122, 14, 88, -59, 74, 27, -18, -105, 36, 54, -68, -69, -91, -51, 70, 105, -53, 42, -18, 80, -121, -85, -22, -64, -127, 54, -105, 50, -102, 75, -94, 99, 10, -128, -96, -2, -1, 0, 113, -91, 124, -23, -38, 114, 104, -45, -127, 62, -71, -26, -2, -36, 8, -68, -51, -28, 83, -70, -2, -112, 25, -76, -43, 55, 16, 47, 35, -126, -95, -98, 1, 17, 79, -47, 101, -23, -38, 100, 7, -103, -80, 23, -29, -68, 58, 2, 21, -16, -80, 71, 12, 66, -49, -33, -27, -125, 44, 83, -6, -81, 36, -15, 83, 48, -114, 66, -69, -95, 6, -119, -91, -114, 101, 40, -101, 72, 111, 7, -9, 28, -124, 30, 102, -79, -127, 65, 79, 86, -2, -48, 59, -62, 40, 125, -91, 69, 118, 110, 26, 33, -38, 61, 20, -98, -81, -11, -44, 32, -14, -118, 108, 78, 33, 42, 11, -28, -5, -86, 90, 96, 121, -123, -30, 90, 89, 89, -106, -70, -88, 79, 26, -81, 72, -44, -76, 100, 125, -90, 119, 97, 80, 55, -54, -2, 114, -103, 77, -103, 125, -95, 23, 115, -94, 69, 83, 111, 19, 79, -90, -94, -62, 32, 91, 91, 29, 66, 51, 109, 67, -102, -95, 75, -126, -91, 127, -62, -60, -1, 0, -102, 59, -74, 107, -76, 37, 114, 61, -25, 109, 96, 62, 88, -125, -62, 28, -36, 31, 0, -115, 12, 75, -88, -79, 87, 55, -92, -90, 84, 45, -49, -1, -38, 0, 12, 3, 1, 0, 2, 0, 3, 0, 0, 0, 16, -1, 0, 48, 54, -101, 116, 60, 10, -66, 22, -38, -113, -4, 85, -34, 31, -66, -42, 65, 60, 114, 76, -15, -114, -88, -3, -111, 86, -87, 32, -122, 26, 59, 36, 84, 27, -56, 120, -101, 87, -14, 107, -127, -104, 87, -82, 105, -110, -52, -1, -60, 0, 33, 17, 1, 1, 1, 0, 2, 2, 2, 2, 3, 0, 0, 0, 0, 0, 0, 0, 1, 0, 17, 33, 49, 16, 65, 97, -79, 81, 113, -95, -31, -16, -1, -38, 0, 8, 1, 3, 1, 1, 63, 16, -39, -13, -126, 46, -58, 88, 121, 124, -110, 35, 119, -114, 35, 125, 113, 124, 87, 115, -58, 121, 1, -84, 29, 115, -22, 32, -29, 50, 65, -65, 44, -72, 46, -39, -31, -105, 2, 20, -40, -106, -78, 6, 86, -18, 109, 35, 106, 81, 54, 25, 75, -86, 77, -30, 92, -32, -11, 14, -94, -23, 54, 30, -82, -118, 67, -116, 77, -104, -92, -99, -71, 3, -123, -79, 0, -31, -11, 12, 0, -101, -20, -17, -88, -100, 28, 36, -29, -12, -113, -59, -4, 77, -7, 83, -41, -81, -33, -51, -82, -74, -38, 57, 35, 113, 109, -112, -118, -26, 105, -39, -59, -87, -29, 23, -50, 78, 117, -33, -9, -11, 127, -1, -60, 0, 30, 17, 1, 1, 1, 0, 2, 3, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 17, 33, 49, 16, 32, 65, 81, 97, -16, -1, -38, 0, 8, 1, 2, 1, 1, 63, 16, -16, 68, 102, -73, -40, -114, -3, 73, 64, 15, 83, 31, 101, 57, -17, -58, -37, 17, 62, -4, 38, 93, -52, -80, 19, 109, -117, -76, 72, -96, 117, 96, 29, 80, 103, 22, 33, -33, 12, 59, -112, -13, 117, 31, -97, 71, 52, -114, 110, -4, 24, 92, 9, -122, 111, -37, 45, -117, 39, -49, -22, 3, 0, -26, -20, 36, -98, 17, -5, -3, -41, -115, -112, 31, 97, -48, -75, 5, 33, 86, 59, -79, 28, -96, 51, -84, -65, -1, -60, 0, 39, 16, 1, 0, 2, 1, 4, 1, 4, 2, 3, 1, 1, 0, 0, 0, 0, 0, 1, 0, 17, 33, 49, 65, 81, 97, 113, 16, -127, -111, -95, -79, -63, 32, -47, -16, -31, -15, -1, -38, 0, 8, 1, 1, 0, 1, 63, 16, -123, -120, 49, 122, 30, 33, -89, -85, 9, 27, -107, -117, 122, 12, -123, -68, -59, -108, 50, 76, 30, -12, 31, 104, 100, 86, 87, 32, -29, 67, 28, 113, 30, 126, -90, 115, -10, -34, 28, 20, -112, 67, 68, 77, 127, -125, 68, 31, -32, 116, 122, 42, 22, -81, -88, 122, -116, -5, 104, 32, -31, -49, 122, -17, 10, 40, 25, 48, -110, -50, 85, -111, 120, 67, -127, -69, 52, 13, -125, 84, 87, 112, 97, 32, -89, 69, 48, 58, 71, -25, -44, 125, 10, 47, 78, -87, -89, -47, -126, 0, 22, -82, -46, -25, -4, -64, -105, 54, 87, 49, 89, 3, 80, 117, -76, -69, -16, 37, -60, 70, 71, -75, 56, -108, -124, 10, 54, 53, 110, -71, 45, 115, -25, -44, 99, -124, 126, -86, -12, -54, 97, 4, 53, 0, -20, 22, 111, -127, -98, 116, -69, -80, -106, 32, -31, 49, -51, -100, -60, -6, -66, -80, 10, 108, -32, -59, 102, 18, -87, -52, -38, 19, -37, -34, -102, 101, 8, -95, 113, 55, 98, -58, -72, 115, 85, -107, -101, -120, 32, 22, -20, 121, -43, -17, -22, -96, -59, -104, -73, -114, 40, -67, 0, -59, -71, 40, 59, -64, 75, -86, 77, 29, -18, 8, 68, -56, 33, -86, 23, -90, -97, 8, 43, 40, 88, 22, 65, 40, -70, -94, 90, -68, 98, 85, -83, 104, 8, 0, -106, -48, 6, -22, -27, 101, -68, 86, 48, -125, 101, 58, 93, -94, 66, -115, 10, -121, 36, 47, 96, 15, -104, -32, 90, -40, 98, -2, 16, -80, 20, -41, -96, 78, 79, 90, 81, -53, 97, 45, -94, 90, -16, 70, -112, 50, -55, 35, -115, 98, 107, 72, 84, 81, 116, -106, 88, -7, -104, 55, 52, 52, 92, 54, -36, 115, 22, 75, 96, 35, 19, 70, 14, 4, -81, -59, -66, -16, 29, -23, -26, -10, 107, -24, 15, -68, 6, -63, 76, 121, -1, 0, -44, -119, 118, -96, 25, -69, 84, 94, -42, -68, -62, 79, 81, 40, -12, -102, 72, -39, -103, -114, -20, 91, -22, -67, -93, -117, 66, 15, -52, 54, -8, -93, 7, -50, -55, -5, -121, -55, 86, -86, -113, 31, -22, -128, -115, -87, -118, -121, -35, -4, -95, -103, -26, 110, -127, 84, 118, -40, 59, 28, 75, 42, -128, -64, 52, 3, 104, 104, 26, 105, 125, 13, -66, 99, 55, 29, 75, -3, 52, 126, -31, 10, 31, -63, 64, -81, 88, 91, -28, 49, -9, 46, -118, -71, -81, 42, 79, -72, 70, 5, 47, -31, 15, -18, 60, -83, 117, 107, 114, 56, 44, 24, -95, -97, 12, -91, 13, -13, 64, 90, -79, -87, -62, 4, -78, 105, 30, 85, 87, 111, 82, -44, -86, -33, -50, 87, -30, -27, 30, -126, -123, 78, 66, -114, -51, 37, -20, 92, 102, -92, 42, 40, 13, 83, -83, -113, 17, 69, 20, 94, -110, -55, 45, 70, 41, -16, 101, -3, 66, -47, -92, 20, -16, -33, -10, 123, 66, 101, 107, 61, -79, -6, -122, 107, 3, 46, 32, 116, 101, 112, 113, 12, 122, -57, 73, -79, -5, -8, -127, -48, -78, -64, -119, 102, 91, -66, -93, -40, -93, -34, 3, 120, -76, 50, -39, 88, -9, -72, -126, 82, 49, -37, -48, -14, -1, 0, 126, -119, -51, 62, 34, -115, -120, -102, 6, 11, -81, 67, 66, 26, 74, -7, -63, 105, -57, 109, 3, 80, 104, -128, -66, -115, 100, -113, 117, 16, -38, -45, 55, -89, -122, 16, 17, -84, 92, -79, -46, 54, -118, -89, 51, -34, -64, 75, 77, 118, -75, -18, -12, 22, -80, 13, -106, 118, 92, -89, -106, -27, -24, 20, -119, -67, -46, -2, -27, -12, -85, 111, 58, -79, -75, -24, 105, 68, -31, 31, 65, -106, -19, 53, -82, -48, -19, 113, 27, 49, 75, 14, -99, -121, 65, 71, -76, 91, -35, 22, -93, 1, -98, -31, -72, -86, -77, 17, 47, -67, -41, 88, -108, -126, -21, 64, 64, 65, -89, -10, 64, 88, 47, 113, 24, 86, -24, 79, -49, 109, 94, 48, 76, -31, -47, -125, -109, 77, 108, 85, 116, 75, -6, 43, 91, 32, -12, -34, -111, 17, 63, 0, 108, -94, -40, 45, 30, 73, 85, 66, 23, 86, -43, 90, -34, -128, -20, -120, -102, 67, 47, 114, -73, -28, -64, 80, 82, 70, -80, 111, 76, 83, -1, 0, 33, -6, 52, -102, 49, -6, 12, 121, 94, 34, -95, 39, -36, -95, -45, 30, 49, 18, -110, 44, -32, 8, 45, 117, -113, -48, -26, 57, 42, 4, -46, -6, 13, -119, 100, 87, -74, 58, -38, 33, 56, 47, 72, 114, -106, 39, 20, -41, -26, 105, 32, 104, 78, -103, -43, 5, 8, -18, -32, 96, 75, 99, -50, -109, -34, 76, 10, 53, -29, 6, -53, -49, -80, -120, -44, 23, 20, 42, -64, -113, -119, -113, -80, 60, -63, 107, 90, -21, 70, -83, -58, 108, 57, -77, -107, -75, -9, -86, -19, -117, -124, 7, 14, 72, 55, 34, 93, 56, 31, -92, 99, 94, 56, 15, 6, -121, -26, 6, 97, -94, -118, -125, -30, 24, 88, 30, 65, -20, 43, 42, 112, -47, -63, -39, -63, -34, -66, 33, -34, 67, -38, -11, -43, 121, -72, 121, 68, -75, 108, -87, -59, 124, -41, -96, 69, 105, 6, -91, 12, -74, 69, -102, 67, 2, 101, -95, 109, 112, 68, 126, -90, 31, 44, -62, -20, 16, 82, -118, 54, 86, 53, -47, 42, 92, 79, 86, -125, 62, 104, 37, -61, -58, -20, -3, -109, 29, -5, 36, -64, -77, 110, 73, 124, 25, 2, 70, -71, -13, 71, -70, 2, -48, -74, -126, -4, 16, -18, -62, -67, -6, 56, -63, 85, -126, -36, -78, -70, 69, 105, -5, -44, 65, -81, 18, -106, 26, -117, 74, 104, 10, -49, -104, -49, -83, -124, -82, 52, -23, -83, -18, -44, 36, -21, 51, 66, -50, -34, 52, -93, 114, 14, 6, -75, 5, -8, 6, 85, 52, -27, 119, 14, -26, -101, 4, 0, 5, -84, -15, -84, 8, -31, -125, 55, 8, 102, -91, 97, -47, -18, 45, -67, -7, -117, 48, -38, 25, -118, -67, 15, 9, -7, -128, -118, 86, -48, -19, 117, 21, -126, 104, -30, 80, -56, -69, 20, 67, 76, -100, 71, -66, 101, -115, -80, 85, -85, -10, -107, -99, -24, 59, 70, -115, 75, 120, 68, 116, -116, -22, 106, 50, 77, -51, -61, -89, 38, 78, -31, 39, 89, -97, 72, 22, -37, 57, -60, 46, -91, -28, -104, 9, -86, -68, -26, 85, -81, -6, 59, -108, -65, 2, -2, -32, -51, 15, 99, -3, -57, -29, -94, -94, -64, 6, -44, -16, 124, 28, 65, -6, -12, -68, -127, -87, -44, 113, -117, -105, 38, -43, 109, -3, -44, -50, 115, 85, 12, -64, -96, 102, 81, 77, -108, -75, 112, 118, -19, -37, -52, 117, 106, 16, 51, -1, -39};

    @Override
    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User saveUser(UserRequestDTO userRequest, String authenticatedUserEmail) {
        try {
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
                Path path = Paths.get(String.valueOf(this.resourceLoader.getResource(Constants.USER_DEFAULT_IMAGE).getFile()));
                avatar.setName(FilenameUtils.removeExtension(path.getFileName().toString()));
                avatar.setExtension(FilenameUtils.getExtension(path.getFileName().toString()));
                avatar.setFile(bytes);
                avatar.setMediaType(MediaType.valueOf(Objects.requireNonNull(Files.probeContentType(path))).toString());
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
