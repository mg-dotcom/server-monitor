package com.servermonitor.monitor.config;

import com.servermonitor.monitor.repository.OperatorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final OperatorRepository operatorRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> operatorRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Operator not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ถ้าเป็น Bean
    @Bean
    public RestClient restClient() {
        return RestClient.create();
    }
    // Spring สร้างให้ "ครั้งเดียว" แล้วแจกใช้ทุกที่ที่ต้องการ (Singleton)
}
