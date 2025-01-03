package com.databaseproject.parkingproject.config;
import com.databaseproject.parkingproject.authorization.LogOutService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity // to enable the web security
@EnableMethodSecurity // to enable the method security such as @PreAuthorize
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogOutService logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/authenticate/**") // list of request that should be permitted
                        .permitAll()
                        .requestMatchers("/api/authenticate/accept/**") // list of request that should be permitted
                        .permitAll()

                        .requestMatchers("/api/v1/SuperAdmin/**") // list of request that should be permitted
                        .hasRole("SUPER_ADMIN")
                        .requestMatchers("/ws/**").permitAll()

                        .anyRequest() // any other request should be authenticated
                        .authenticated()

                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // to make the session stateless
                )
                .authenticationProvider(authenticationProvider) // to add the authentication provider
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // to add the filter before the UsernamePasswordAuthenticationFilter
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout") // to set the logout url
                        .addLogoutHandler(logoutHandler) // to add the logout handler
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()) // to set the logout success handler
                );

        return http.build();
    }

}

