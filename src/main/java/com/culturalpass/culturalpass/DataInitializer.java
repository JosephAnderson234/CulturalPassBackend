package com.culturalpass.culturalpass;

import com.culturalpass.culturalpass.Event.domain.Event;
import com.culturalpass.culturalpass.Event.domain.EventStatus;
import com.culturalpass.culturalpass.Event.domain.EventType;
import com.culturalpass.culturalpass.Event.infrastructure.EventRepository;
import com.culturalpass.culturalpass.User.domain.User;
import com.culturalpass.culturalpass.User.domain.UserRole;
import com.culturalpass.culturalpass.User.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Component
public class DataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

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
        // 1. Inicializar Eventos
        initializeEvents();
        
        // 2. Inicializar Usuarios
        initializeUsers();
    }

    private void initializeEvents() {
        if (eventRepository.count() == 0) {
            System.out.println("Inicializando eventos de prueba...");

            // Evento 1: Concierto de Rock
            Event event1 = Event.builder()
                    .title("Concierto de Rock")
                    .description("Un emocionante concierto de rock con bandas locales.")
                    .location("Auditorio Nacional")
                    .startDate(OffsetDateTime.of(2025, 11, 22, 20, 0, 0, 0, ZoneOffset.UTC))
                    .endDate(OffsetDateTime.of(2025, 11, 22, 23, 0, 0, 0, ZoneOffset.UTC))
                    .tags(List.of("música", "concierto", "rock"))
                    .type(EventType.CONCIERTO)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(500)
                    .imageUrl("https://imgmedia.larepublica.pe/1000x590/larepublica/original/2025/10/17/68f30686b3f6c319b802ec28.webp")
                    .costEntry(30.0)
                    .build();
            eventRepository.save(event1);
            System.out.println("✓ Evento creado: Concierto de Rock");

            // Evento 2: Obra de Teatro
            Event event2 = Event.builder()
                    .title("Romeo y Julieta")
                    .description("Clásica obra de Shakespeare interpretada por compañía nacional de teatro.")
                    .location("Teatro Municipal")
                    .startDate(OffsetDateTime.of(2025, 12, 5, 19, 30, 0, 0, ZoneOffset.UTC))
                    .endDate(OffsetDateTime.of(2025, 12, 5, 21, 30, 0, 0, ZoneOffset.UTC))
                    .tags(List.of("teatro", "clásico", "drama"))
                    .type(EventType.OBRA_DE_TEATRO)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(200)
                    .imageUrl("https://example.com/romeo-julieta.jpg")
                    .costEntry(25.0)
                    .build();
            eventRepository.save(event2);
            System.out.println("✓ Evento creado: Romeo y Julieta");

            // Evento 3: Exposición de Arte
            Event event3 = Event.builder()
                    .title("Exposición de Arte Moderno")
                    .description("Muestra de arte contemporáneo con obras de artistas emergentes.")
                    .location("Galería de Arte Contemporáneo")
                    .startDate(OffsetDateTime.of(2025, 11, 15, 10, 0, 0, 0, ZoneOffset.UTC))
                    .endDate(OffsetDateTime.of(2025, 12, 15, 18, 0, 0, 0, ZoneOffset.UTC))
                    .tags(List.of("arte", "exposición", "moderno"))
                    .type(EventType.EXPOSICION)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(100)
                    .imageUrl("https://example.com/arte-moderno.jpg")
                    .costEntry(0.0)
                    .build();
            eventRepository.save(event3);
            System.out.println("✓ Evento creado: Exposición de Arte Moderno");

            // Evento 4: Taller de Fotografía
            Event event4 = Event.builder()
                    .title("Taller de Fotografía Digital")
                    .description("Aprende técnicas básicas y avanzadas de fotografía digital.")
                    .location("Centro Cultural")
                    .startDate(OffsetDateTime.of(2025, 11, 28, 14, 0, 0, 0, ZoneOffset.UTC))
                    .endDate(OffsetDateTime.of(2025, 11, 28, 18, 0, 0, 0, ZoneOffset.UTC))
                    .tags(List.of("taller", "fotografía", "educación"))
                    .type(EventType.TALLER)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(30)
                    .imageUrl("https://example.com/taller-foto.jpg")
                    .costEntry(15.0)
                    .build();
            eventRepository.save(event4);
            System.out.println("✓ Evento creado: Taller de Fotografía Digital");

            // Evento 5: Festival de Jazz
            Event event5 = Event.builder()
                    .title("Festival de Jazz 2025")
                    .description("Festival anual con los mejores músicos de jazz nacional e internacional.")
                    .location("Parque Central")
                    .startDate(OffsetDateTime.of(2025, 12, 10, 18, 0, 0, 0, ZoneOffset.UTC))
                    .endDate(OffsetDateTime.of(2025, 12, 10, 23, 59, 0, 0, ZoneOffset.UTC))
                    .tags(List.of("música", "jazz", "festival"))
                    .type(EventType.CONCIERTO)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(1000)
                    .imageUrl("https://example.com/jazz-festival.jpg")
                    .costEntry(40.0)
                    .build();
            eventRepository.save(event5);
            System.out.println("✓ Evento creado: Festival de Jazz 2025");

            // Evento 6: Cine al Aire Libre
            Event event6 = Event.builder()
                    .title("Ciclo de Cine Clásico")
                    .description("Proyección de películas clásicas bajo las estrellas.")
                    .location("Plaza Mayor")
                    .startDate(OffsetDateTime.of(2025, 11, 25, 20, 30, 0, 0, ZoneOffset.UTC))
                    .endDate(OffsetDateTime.of(2025, 11, 25, 23, 0, 0, 0, ZoneOffset.UTC))
                    .tags(List.of("cine", "película", "al aire libre"))
                    .type(EventType.PROYECCION)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(300)
                    .imageUrl("https://example.com/cine-aire-libre.jpg")
                    .costEntry(0.0)
                    .build();
            eventRepository.save(event6);
            System.out.println("✓ Evento creado: Ciclo de Cine Clásico");

            System.out.println("=== Inicialización de eventos completada ===\n");
        } else {
            System.out.println("Los eventos ya están inicializados.\n");
        }
    }

    private void initializeUsers() {
        System.out.println("Inicializando usuarios...");

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
            System.out.println("✓ Usuario administrador creado: " + adminEmail);
        } else {
            System.out.println("✓ Usuario administrador ya existe: " + adminEmail);
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
            System.out.println("✓ Usuario de prueba creado: a@b.test");
        }

        System.out.println("=== Inicialización de usuarios completada ===");
    }
}