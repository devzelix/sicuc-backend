package com.culturacarabobo.sicuc.backend.services;

import io.jsonwebtoken.Claims;
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

@Service
@SuppressWarnings("null") // ← elimina los avisos de null-safety genéricos
public class JwtService {

    private final String secretKey;
    private final long jwtExpiration;
    private final long refreshExpiration;

    public JwtService(
        @Value("${application.security.jwt.secret-key}") String secretKey,
        @Value("${application.security.jwt.access-token.expiration}") long jwtExpiration,
        @Value("${application.security.jwt.refresh-token.expiration}") long refreshExpiration
    ) {
        this.secretKey = secretKey;
        this.jwtExpiration = jwtExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    // === Extract username ===
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // === Generate access token ===
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "access");
        return buildToken(claims, userDetails, jwtExpiration);
    }

    // === Generate refresh token ===
    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");
        return buildToken(claims, userDetails, refreshExpiration);
    }

    // === Validate token ===
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // === Extract claim ===
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // === Build JWT ===
    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey()) // HS256 se infiere automáticamente
                .compact();
    }

    // === Parse claims ===
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey()) // recibe SecretKey
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // === Obtain key ===
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
