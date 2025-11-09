package com.culturacarabobo.sicuc.backend.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException; // Import for Javadoc
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Service responsible for all JSON Web Token (JWT) operations.
 * <p>
 * This includes generating, parsing, and validating access and refresh tokens
 * using properties injected from the application configuration.
 */
@Service
@SuppressWarnings("null")
public class JwtService {

    private final String secretKey;
    private final long jwtExpiration;
    private final long refreshExpiration;

    /**
     * Constructs the JwtService with secrets and expirations injected from
     * application properties.
     *
     * @param secretKey       The Base64 encoded secret key for signing tokens.
     * @param jwtExpiration   The expiration time for access tokens (in
     * milliseconds).
     * @param refreshExpiration The expiration time for refresh tokens (in
     * milliseconds).
     */
    public JwtService(
            @Value("${application.security.jwt.secret-key}") String secretKey,
            @Value("${application.security.jwt.access-token.expiration}") long jwtExpiration,
            @Value("${application.security.jwt.refresh-token.expiration}") long refreshExpiration
    ) {
        this.secretKey = secretKey;
        this.jwtExpiration = jwtExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    /**
     * Extracts the username (subject) from a given JWT.
     *
     * @param token The JWT string.
     * @return The username (subject) stored in the token.
     * @throws ExpiredJwtException If the token is expired.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Generates a short-lived "access" token for a user.
     * <p>
     * Includes a custom "type" claim to distinguish it from a refresh token.
     *
     * @param userDetails The user details object to build the token from.
     * @return A signed JWT access token string.
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return buildToken(claims, userDetails, jwtExpiration);
    }

    /**
     * Generates a long-lived "refresh" token for a user.
     * <p>
     * Includes a custom "type" claim to distinguish it from an access token.
     *
     * @param userDetails The user details object to build the token from.
     * @return A signed JWT refresh token string.
     */
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return buildToken(claims, userDetails, refreshExpiration);
    }

    /**
     * Validates a JWT.
     * <p>
     * Checks if the username in the token matches the UserDetails' username
     * AND the token is not expired.
     *
     * @param token       The JWT string to validate.
     * @param userDetails The user to validate against.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * A generic method to extract a specific claim from a token's payload.
     *
     * @param <T>            The type of the claim.
     * @param token          The JWT string.
     * @param claimsResolver A function to apply to the claims (e.g.,
     * {@code Claims::getSubject}).
     * @return The resolved claim.
     * @throws ExpiredJwtException If the token is expired.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Checks if a token's expiration date is before the current time.
     *
     * @param token The JWT string.
     * @return {@code true} if the token is expired, {@code false} otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the token.
     *
     * @param token The JWT string.
     * @return The {@link Date} of expiration.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Helper method to build a JWT with a specific type, subject, and expiration.
     *
     * @param extraClaims Extra claims to add to the payload (e.g., "type").
     * @param userDetails The user to whom the token belongs.
     * @param expiration  The expiration time in milliseconds.
     * @return A compact, signed JWT string.
     */
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey()) // HMAC-SHA algorithm is inferred
                .compact();
    }

    /**
     * Parses the token, verifies the signature using the secret key, and returns
     * all claims.
     *
     * @param token The JWT string.
     * @return The {@link Claims} payload.
     * @throws ExpiredJwtException If the token is expired.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Decodes the Base64 {@code secretKey} string into a {@link SecretKey}
     * object
     * suitable for HMAC-SHA algorithms.
     *
     * @return The HMAC-SHA {@link SecretKey}.
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}