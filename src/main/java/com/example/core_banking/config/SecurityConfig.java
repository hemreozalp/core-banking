package com.example.core_banking.config;

import com.example.core_banking.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Kritik: Metod seviyesinde güvenlik (Ownership check) sağlar
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Token almak için giriş ucumuzu herkese açıyoruz
                        .requestMatchers("/api/auth/login").permitAll()

                        // Yol haritandaki yeni kurumsal rollere göre kısıtlamalar:
                        .requestMatchers(HttpMethod.POST, "/api/customers").hasRole("TELLER") // Gişe Görevlisi açar
                        .requestMatchers(HttpMethod.POST, "/api/accounts").hasRole("TELLER")
                        .requestMatchers("/api/transfers").hasAnyRole("CUSTOMER", "TELLER")
                        .requestMatchers("/api/audit/**").hasRole("AUDITOR") // Müfettiş ucu

                        .anyRequest().authenticated()
                )
                // Oturum yönetimini tamamen STATELESS yapıyoruz (Sunucuda session tutulmaz)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Bizim yazdığımız JWT filtresini standart UsernamePassword filtresinin önüne koyuyoruz
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
