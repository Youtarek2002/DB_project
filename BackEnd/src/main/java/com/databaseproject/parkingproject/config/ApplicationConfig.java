package com.databaseproject.parkingproject.config;

import com.databaseproject.parkingproject.dao.UserDaoImpl;
import com.databaseproject.parkingproject.entity.Users;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Users> userRowMapper = (rs, rowNum) -> Users.builder()
            .id(rs.getInt("id"))
            .fname(rs.getString("fname"))
            .lname(rs.getString("lname"))
            .username(rs.getString("username"))
            .email(rs.getString("email"))
            .phone(rs.getString("phone"))
            .password(rs.getString("password"))
            .paymentMethod(rs.getString("payment_method"))
            .role(Users.Role.valueOf(rs.getString("role")))
            .licensePlate(rs.getString("license_plate"))
            .build();

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            String query = "SELECT * FROM users WHERE username = ?";
            try {
                Users user = jdbcTemplate.queryForObject(query, userRowMapper, username);

                // Assuming Users implements UserDetails
                return user;
            } catch (Exception e) {
                throw new UsernameNotFoundException("User not found: " + username);
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}


