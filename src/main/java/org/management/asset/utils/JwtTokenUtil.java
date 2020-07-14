package org.management.asset.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.management.asset.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author Haytham DAHRI
 * Authenticating users by their emails and password
 */
@Component
public class JwtTokenUtil implements Serializable {

    public static final long JWT_TOKEN_VALIDITY = ((60 * 60 * 24) * 30);
    private static final long serialVersionUID = -2550185165626007488L;

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private UserService userService;

    /**
     * retrieve email from jwt token
     *
     * @param token: User token
     * @return String
     */
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * retrieve expiration date from jwt token
     *
     * @param token: User token
     * @return Date
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Allows to retrieve any information from token
     * The secret key is required to proceed
     *
     * @param token: User token
     * @return Claims
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    /**
     * check if the token has expired
     *
     * @param token: User token
     * @return Boolean
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * generate token for user
     *
     * @param userDetails: User details
     * @return String
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        this.userService.getUserByEmail(userDetails.getUsername()).getGroups().forEach(System.out::println);
        System.out.println(userDetails.getUsername());
        claims.put("groups", this.userService.getUserByEmail(userDetails.getUsername()).getGroups());
        return doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * while creating the token -
     * 1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
     * 2. Sign the JWT using the HS512 algorithm and secret key.
     * 3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
     * compaction of the JWT to a URL-safe string
     *
     * @param claims:  User claims
     * @param subject: Token subject
     * @return String
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    /**
     * validate token
     *
     * @param token:       User token
     * @param userDetails: UserDetails object
     * @return Boolean
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String email = getEmailFromToken(token);
        return (email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
