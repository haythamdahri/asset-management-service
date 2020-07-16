package org.management.asset.dao;

import org.management.asset.bo.RoleType;
import org.management.asset.bo.User;
import org.management.asset.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

/**
 * @author Haytam DAHRI
 */
@Repository
@RepositoryRestResource
@CrossOrigin(value = "*")
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(@Param("email") String email);

    Optional<User> findByUsername(@Param("username") String username);

    @RestResource(path = "existsByEmail", rel = "checkUserExisting")
    boolean existsByEmail(@Param("email") String email);

    Optional<User> findByEmailAndActiveIsTrue(@Param("email") String email);

    Optional<User> findByUsernameAndActiveIsTrue(@Param("username") String username);

    Optional<User> findByToken(@Param("token") String token);

    @Query(value = "select u from User u inner join u.roles r where r.roleName in :roleNames and size(u.roles) = 1")
    List<User> findBySpecificRoles(@Param("roleNames") List<RoleType> roleName);

    @Query(value = "select u from User u inner join u.roles r where r.roleName in :roleNames and size(u.roles) = 1 and " +
            "(CONCAT(u.id, '') like %:search% or lower(u.username) like %:search% or lower(u.email) like %:search% or lower(u.location) like %:search%)")
    List<User> findBySpecificRolesAndSearch(@Param("roleNames") List<RoleType> roleName, @Param(value = "search") String search);

    @Query(value = "select u from User u inner join u.roles r where r.roleName in :roleNames and size(u.roles) = 1")
    Page<User> findBySpecificRolesPage(@PageableDefault Pageable pageable, @Param("roleNames") List<RoleType> roleName);

    @Query(value = "select u from User u where u.email <> :excludedUserEmail")
    Page<User> findAllWithUserExclusion(@PageableDefault Pageable pageable, @Param("excludedUserEmail") String excludedUserEmail);

    @Query(value = "select u from User u where (lower(u.username) like %:search% " +
            "or lower(u.email) like %:search% or lower(u.firstName) like %:search% or lower(u.lastName) like %:search% " +
            "or lower(u.city) like %:search% or lower(u.country) like %:search% or lower(u.employeeNumber) like %:search% " +
            "or lower(u.jobTitle) like %:search% or lower(u.notes) like %:search% or lower(u.phone) like %:search% " +
            "or lower(u.title) like %:search% or lower(u.website) like %:search% or lower(u.zip) like %:search% " +
            "or lower(u.department.name) like %:search% or lower(u.location.name) like %:search%) and u.email <> :excludedUserEmail")
    Page<User> findBySearch(@PageableDefault Pageable pageable, @Param(value = "search") String search, @Param("excludedUserEmail") String excludedUserEmail);

    @Query(value = "SELECT u.id, u.username, u.firstName, u.lastName, u.email FROM User u WHERE u.id = :id")
    Optional<UserDTO> findCustomUserById(@Param("id") Long id);

    @Query(value = "SELECT new org.management.asset.dto.UserDTO(u.id, u.username, u.firstName, u.lastName, u.email) FROM User u")
    List<UserDTO> findCustomUsers();

}
