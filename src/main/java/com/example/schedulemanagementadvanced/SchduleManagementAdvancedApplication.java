package com.example.schedulemanagementadvanced;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing

@SpringBootApplication
public class SchduleManagementAdvancedApplication {

    public static void main(String[] args) {
        SpringApplication.run(SchduleManagementAdvancedApplication.class, args);
    }

}
