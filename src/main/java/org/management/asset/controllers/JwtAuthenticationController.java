package org.management.asset.controllers;

import org.management.asset.bo.User;
import org.management.asset.services.UserService;
import org.management.asset.utils.JwtTokenUtil;
import org.management.asset.dto.JwtRequestDTO;
import org.management.asset.dto.JwtResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author Haytham DAHRI
 */
@RestController
@RequestMapping(path = "/api/v1/auth")
@CrossOrigin("*")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    /**
     * @param authenticationRequest: Authentication request body for JWT
     * @return ResponseEntity<JwtResponse>
     */
    @PostMapping(value = "/")
    public ResponseEntity<JwtResponseDTO> createAuthenticationToken(@RequestBody JwtRequestDTO authenticationRequest) {
        authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        // Set user last Login to now
        User user = this.userService.getUserByEmail(userDetails.getUsername());
        user.setLastLogin(LocalDateTime.now());
        this.userService.saveUser(user);
        return ResponseEntity.ok(new JwtResponseDTO(token));
    }

    /**
     * @param email:    User email
     * @param password: User Password
     */
    private void authenticate(String email, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new DisabledException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
    }
}

