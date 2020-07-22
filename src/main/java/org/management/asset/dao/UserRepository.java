package org.management.asset.dao;

import org.management.asset.bo.RoleType;
import org.management.asset.bo.User;
import org.management.asset.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Haytam DAHRI
 */
@Repository
@RepositoryRestResource(path = "users")
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(final String email);

    Optional<User> findByIdNotAndEmail(final String id, final String email);

    Optional<User> findByEmployeeNumber(final String employeeNumber);

    Optional<User> findByIdNotAndEmployeeNumber(final String id, final String employeeNumber);

    Optional<User> findByUsername(final String username);

    @RestResource(path = "existsByEmail", rel = "checkUserExisting")
    boolean existsByEmail(final String email);

    Optional<User> findByEmailAndActiveIsTrue(final String email);

    Optional<User> findByUsernameAndActiveIsTrue(final String username);

    Optional<User> findByToken(final String token);

    @Query(value = "{'roles': { $elemMatch: { 'roleName' : {$in: [?0]} }, $size: {$eq: 1}}}")
    List<User> findBySpecificRoles(List<RoleType> roleName);

    @Query(value = "{{'roles': { $elemMatch: { 'roleName' : {$in: [?0]} }, $size: {$eq: 1}}}, $or: [{'id': {$regex : ?1, $options: 'i'}}, {'username': {$regex : ?1, $options: 'i'}}, {'email': {$regex : ?1, $options: 'i'}}, {'location.name': {$regex : ?1, $options: 'i'}}]}")
    List<User> findBySpecificRolesAndSearch(List<RoleType> roleName, String search);

    @Query(value = "{'roles': { $elemMatch: { 'roleName' : {$in: [?0]} }, $size: {$eq: 1}}}")
    Page<User> findBySpecificRolesPage(List<RoleType> roleName, @PageableDefault Pageable pageable);

    @Query(value = "{'email': {$ne: ?0}}")
    Page<User> findAllWithUserExclusion(final String excludedUserEmail, @PageableDefault Pageable pageable);

    @Query(value = "{$or: [{'username': {$regex : '.*?0.*', $options: 'i'}}, {'email': {$regex : '.*?0.*', $options: 'i'}}, {'firstName': {$regex : '.*?0.*', $options: 'i'}}, " +
            "{'lastName': {$regex : '.*?0.*', $options: 'i'}}, {'city': {$regex : '.*?0.*', $options: 'i'}}, {'country': {$regex : '.*?0.*', $options: 'i'}}, " +
            "{'employeeNumber': {$regex : '.*?0.*', $options: 'i'}}, {'jobTitle': {$regex : '.*?0.*', $options: 'i'}}, {'notes': {$regex : '.*?0.*', $options: 'i'}}, " +
            "{'phone': {$regex : '.*?0.*', $options: 'i'}}, {'title': {$regex : '.*?0.*', $options: 'i'}}, {'website': {$regex : '.*?0.*', $options: 'i'}}, {'zip': {$regex : '.*?0.*', $options: 'i'}}], " +
            "'email': {$ne: ?1}}")
    Page<User> findBySearch(String search, String excludedUserEmail, @PageableDefault Pageable pageable);

    @Query(value = "{'id': ?0}", fields = "{id: 0, username: 0, firstName: 0, lastName: 0, email: 0}")
    Optional<UserDTO> findCustomUserById(String id);

    @Query(value = "{}", fields = "{id: 1, username: 1, firstName: 1, lastName: 1, email: 1}")
    List<UserDTO> findCustomUsers();

}
