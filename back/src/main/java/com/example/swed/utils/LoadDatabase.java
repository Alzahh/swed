package com.example.swed.utils;

import com.example.swed.models.User;
import com.example.swed.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class LoadDatabase {


    @Bean
    CommandLineRunner initDatabase(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        if (!userRepo.existsByEmail("admin@admin.ee")) {
            return args -> {
                userRepo.save(new User("admin", "admin@admin.ee", passwordEncoder.encode("admin"), "ADMIN"));

            };
        }
        return null;
    }
}
