package com.culturalpass.culturalpass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CulturalPassApplication {

    public static void main(String[] args) {
        SpringApplication.run(CulturalPassApplication.class, args);
    }

}
