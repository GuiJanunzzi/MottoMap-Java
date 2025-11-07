package br.com.fiap.mottomap.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.com.fiap.mottomap.filter.AuthorizationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private AuthorizationFilter authorizationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // libera o H2 do CSRF
                        .disable())
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable())) // permite exibir o console em iframe
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/usuario/registrar").permitAll()
                        .requestMatchers(HttpMethod.POST, "/usuario/login").permitAll()
                        // Swagger
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // H2 Console
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
