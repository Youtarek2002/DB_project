package com.databaseproject.parkingproject.authorization;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogOutService implements LogoutHandler {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        jwt = authHeader.substring(7);

        // Check if the token exists in the database
        String findTokenQuery = "SELECT COUNT(*) FROM tokens WHERE token = ?";
        Integer count = jdbcTemplate.queryForObject(findTokenQuery, Integer.class, jwt);

        if (count != null && count > 0) {
            // Mark the token as revoked
            String updateTokenQuery = "UPDATE tokens SET revoked = TRUE WHERE token = ?";
            jdbcTemplate.update(updateTokenQuery, jwt);

            // Clear the security context
            SecurityContextHolder.clearContext();
        }
    }
}
