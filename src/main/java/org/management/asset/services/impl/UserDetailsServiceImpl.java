package org.management.asset.services.impl;

import org.management.asset.bo.User;
import org.management.asset.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Haytam DAHRI
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) {
        // Fetch user from database using his email
        User user = this.userService.getActiveUser(email);
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (user != null) {
            // Check user roles
            if (user.getRoles() != null) {
                user.getRoles().forEach(role ->
                        grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName().name()))
                );
            }
            // Check user group roles
            if (user.getGroups() != null) {
                user.getGroups().forEach(group -> group.getRoles().forEach(role ->
                        grantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName().name()))
                ));
            }
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), grantedAuthorities);
        }
        throw new UsernameNotFoundException("No user found with " + email);
    }
}
