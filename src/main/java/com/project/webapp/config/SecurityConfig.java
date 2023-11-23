package com.project.webapp.config;

import com.project.webapp.service.DatabaseService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig{
    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;
    @Autowired
    private DatabaseService databaseService;
    private static final Logger logger = LogManager.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers(new AntPathRequestMatcher("/healthz","GET")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/","GET")).permitAll()
                        .anyRequest()
                        .authenticated());
        http.httpBasic(withDefaults())
                .authenticationProvider(customAuthenticationProvider)
                .exceptionHandling((exceptions) -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint())
                );
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint customAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            if (!databaseService.isServerRunning()) {
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            } else {
                logger.warn("Authorization failed");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }
        };
    }


}
