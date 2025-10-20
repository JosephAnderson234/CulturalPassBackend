package com.culturalpass.culturalpass;

import com.culturalpass.culturalpass.User.domain.User;
import com.culturalpass.culturalpass.User.domain.UserRole;
import com.culturalpass.culturalpass.User.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.cellphone}")
    private String adminCellphone;

    @Value("${admin.firstName}")
    private String adminFirstName;

    @Value("${admin.lastName}")
    private String adminLastName;

    @PostConstruct
    public void initializeData() {
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = User.builder()
                    .firstName(adminFirstName)
                    .lastName(adminLastName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(UserRole.ADMIN)
                    .cellphone(adminCellphone)
                    .build();
            userRepository.save(admin);
            System.out.println("Usuario administrador creado por defecto: " + adminEmail);
        } else {
            System.out.println("Usuario administrador ya existe: " + adminEmail);
        }
        if (!userRepository.existsByEmail("a@b.test")) {
            User user = User.builder()
                    .firstName("Usuario")
                    .lastName("De Prueba")
                    .email("a@b.test")
                    .password(passwordEncoder.encode("123456789"))
                    .role(UserRole.CLIENTE)
                    .cellphone("600600600")
                    .build();
            userRepository.save(user);
            System.out.println("Usuario de prueba creado: a@b.test");

        }
    }
}