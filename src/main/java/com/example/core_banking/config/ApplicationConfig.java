package com.example.core_banking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class ApplicationConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails teller = User.builder()
                .username("gise_ayse")
                .password("{noop}gise123")
                .roles("TELLER")
                .build();

        UserDetails customer = User.builder()
                .username("customer_1")
                .password("{noop}bakiye123")
                .roles("CUSTOMER")
                .build();

        UserDetails auditor = User.builder()
                .username("mufettis_kemal")
                .password("{noop}mufettis123")
                .roles("AUDITOR")
                .build();

        return new InMemoryUserDetailsManager(teller, customer, auditor);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}