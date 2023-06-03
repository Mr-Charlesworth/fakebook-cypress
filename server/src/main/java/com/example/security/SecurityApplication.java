package com.example.security;

import com.example.security.properties.RsaKeyProperties;
import com.example.security.domain.User;
import com.example.security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableConfigurationProperties({RsaKeyProperties.class})
@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder encoder) {
        return args -> {
            userRepository.save(new User("user", encoder.encode("password"), "ROLE_USER"));
            userRepository.save(new User("admin", encoder.encode("password"), "ROLE_ADMIN,ROLE_USER"));
            userRepository.save(new User("anon", encoder.encode("password"), ""));
        };
    }

}
