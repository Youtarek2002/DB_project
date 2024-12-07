package com.databaseproject.parkingproject.config;


import com.databaseproject.parkingproject.entity.Users;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JdbcOperations jdbcTemplate;

    private static final String Security_Key = "088TI3YCXQd26NOxZJXXKwFAIyojGuY2Y6HX7kVb9ftVp3Rz"; //
    private Claims extractAllClaims(String token) {

        return Jwts.
                parserBuilder().
                setSigningKey(getSigningKey()).
                build().
                parseClaimsJws(token).
                getBody();
    }

    public Key getSigningKey() {
        byte[] signingKey = Decoders.BASE64.decode(Security_Key);
        return Keys.hmacShaKeyFor(signingKey);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public String generateToken(
            Map<String, Object> extraClaims,
            Users userDetails
    ) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername() )&& !isTokenExpired(token));
    }


    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    private boolean isTokenExpired(String token) {
        // Fetch token details using JdbcTemplate
        String query = "SELECT revoked FROM tokens WHERE token = ?";
        Optional<Boolean> isRevoked = Optional.ofNullable(
                jdbcTemplate.queryForObject(query, Boolean.class, token)
        );

        // Check if the token is revoked or expired
        return extractExpiration(token).before(new Date()) || isRevoked.orElse(false);
    }


}

