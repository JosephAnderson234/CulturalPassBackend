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
import java.util.Random;

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

            OffsetDateTime now = OffsetDateTime.now();
            Random random = new Random();

            // Evento 1: Concierto de Rock
            OffsetDateTime start1 = now.minusHours(random.nextInt(48)); // Ya empezó hace 0-47 horas
            Event event1 = Event.builder()
                    .title("Concierto de Rock")
                    .description("Un emocionante concierto de rock con bandas locales.")
                    .location("Auditorio Nacional")
                    .startDate(start1)
                    .endDate(start1.plusHours(random.nextInt(4) + 2))
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
            OffsetDateTime start2 = now.plusHours(random.nextInt(72) - 24); // Rango: -24 a +48 horas
            Event event2 = Event.builder()
                    .title("Romeo y Julieta")
                    .description("Clásica obra de Shakespeare interpretada por compañía nacional de teatro.")
                    .location("Teatro Municipal")
                    .startDate(start2)
                    .endDate(start2.plusHours(random.nextInt(3) + 1))
                    .tags(List.of("teatro", "clásico", "drama"))
                    .type(EventType.OBRA_DE_TEATRO)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(200)
                    .imageUrl("https://www.elcorteingles.es/entradas/blog/app/uploads/2022/07/teatro.jpg")
                    .costEntry(25.0)
                    .build();
            eventRepository.save(event2);
            System.out.println("✓ Evento creado: Romeo y Julieta");

            // Evento 3: Exposición de Arte
            OffsetDateTime start3 = now.plusDays(random.nextInt(14) - 7); // Rango: -7 a +7 días
            Event event3 = Event.builder()
                    .title("Exposición de Arte Moderno")
                    .description("Muestra de arte contemporáneo con obras de artistas emergentes.")
                    .location("Galería de Arte Contemporáneo")
                    .startDate(start3)
                    .endDate(start3.plusDays(random.nextInt(20) + 10))
                    .tags(List.of("arte", "exposición", "moderno"))
                    .type(EventType.EXPOSICION)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(100)
                    .imageUrl("https://www.singulart.com/blog/wp-content/uploads/2025/04/Art-Basel-1.jpeg")
                    .costEntry(0.0)
                    .build();
            eventRepository.save(event3);
            System.out.println("✓ Evento creado: Exposición de Arte Moderno");

            // Evento 4: Taller de Fotografía
            OffsetDateTime start4 = now.plusHours(random.nextInt(120) - 48); // Rango: -48 a +72 horas
            Event event4 = Event.builder()
                    .title("Taller de Fotografía Digital")
                    .description("Aprende técnicas básicas y avanzadas de fotografía digital.")
                    .location("Centro Cultural")
                    .startDate(start4)
                    .endDate(start4.plusHours(random.nextInt(4) + 3))
                    .tags(List.of("taller", "fotografía", "educación"))
                    .type(EventType.TALLER)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(30)
                    .imageUrl("https://andanafoto.com/wp-content/uploads/2017/09/Taller-de-fotograf%C3%ADa-intantil-y-juvenil-andanafoto-3.jpg")
                    .costEntry(15.0)
                    .build();
            eventRepository.save(event4);
            System.out.println("✓ Evento creado: Taller de Fotografía Digital");

            // Evento 5: Festival de Jazz
            OffsetDateTime start5 = now.plusDays(random.nextInt(20) - 5); // Rango: -5 a +15 días
            Event event5 = Event.builder()
                    .title("Festival de Jazz 2025")
                    .description("Festival anual con los mejores músicos de jazz nacional e internacional.")
                    .location("Parque Central")
                    .startDate(start5)
                    .endDate(start5.plusHours(random.nextInt(6) + 4))
                    .tags(List.of("música", "jazz", "festival"))
                    .type(EventType.CONCIERTO)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(1000)
                    .imageUrl("https://montrealcentreville.ca/wp-content/uploads/2024/06/PasseportFest2.jpg")
                    .costEntry(40.0)
                    .build();
            eventRepository.save(event5);
            System.out.println("✓ Evento creado: Festival de Jazz 2025");

            // Evento 6: Cine al Aire Libre
            OffsetDateTime start6 = now.plusHours(random.nextInt(96) - 24); // Rango: -24 a +72 horas
            Event event6 = Event.builder()
                    .title("Ciclo de Cine Clásico")
                    .description("Proyección de películas clásicas bajo las estrellas.")
                    .location("Plaza Mayor")
                    .startDate(start6)
                    .endDate(start6.plusHours(random.nextInt(2) + 2))
                    .tags(List.of("cine", "película", "al aire libre"))
                    .type(EventType.PROYECCION)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(300)
                    .imageUrl("https://aquaf.s3.sa-east-1.amazonaws.com/wp-content/uploads/2024/03/05183145/CINE-AQUAFONDO-6.jpg")
                    .costEntry(0.0)
                    .build();
            eventRepository.save(event6);
            System.out.println("✓ Evento creado: Ciclo de Cine Clásico");

            // Evento 7: Conferencia de Tecnología
            OffsetDateTime start7 = now.plusDays(random.nextInt(30) - 10); // Rango: -10 a +20 días
            Event event7 = Event.builder()
                    .title("Conferencia Tech Summit 2025")
                    .description("Conferencia sobre las últimas tendencias en tecnología e innovación.")
                    .location("Centro de Convenciones")
                    .startDate(start7)
                    .endDate(start7.plusDays(random.nextInt(2) + 1).plusHours(random.nextInt(8)))
                    .tags(List.of("tecnología", "innovación", "conferencia"))
                    .type(EventType.CONFERENCIA)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(800)
                    .imageUrl("https://www.pabloyglesias.com/wp-content/uploads/2019/05/Blockchain-AI-Summit.jpg")
                    .costEntry(50.0)
                    .build();
            eventRepository.save(event7);
            System.out.println("✓ Evento creado: Conferencia Tech Summit 2025");

            // Evento 8: Feria de Artesanías
            OffsetDateTime start8 = now.plusDays(random.nextInt(16) - 3); // Rango: -3 a +13 días
            Event event8 = Event.builder()
                    .title("Feria Nacional de Artesanías")
                    .description("Exposición y venta de artesanías tradicionales de todo el país.")
                    .location("Plaza de Armas")
                    .startDate(start8)
                    .endDate(start8.plusDays(random.nextInt(5) + 3))
                    .tags(List.of("artesanía", "cultura", "feria"))
                    .type(EventType.FERIA)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(500)
                    .imageUrl("https://elperuano.pe/fotografia/thumbnail/2023/07/16/000256958M.jpg")
                    .costEntry(0.0)
                    .build();
            eventRepository.save(event8);
            System.out.println("✓ Evento creado: Feria Nacional de Artesanías");

            // Evento 9: Taller de Pintura
            OffsetDateTime start9 = now.plusHours(random.nextInt(144) - 72); // Rango: -72 a +72 horas
            Event event9 = Event.builder()
                    .title("Taller de Acuarela para Principiantes")
                    .description("Descubre las técnicas básicas de la acuarela en un ambiente relajado.")
                    .location("Escuela de Arte")
                    .startDate(start9)
                    .endDate(start9.plusHours(random.nextInt(3) + 2))
                    .tags(List.of("arte", "pintura", "taller"))
                    .type(EventType.TALLER)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(20)
                    .imageUrl("https://offloadmedia.feverup.com/cdmxsecreta.com/wp-content/uploads/2023/05/21020937/donde-tomar-clases-de-pintura-en-cdmx-condesa.jpg")
                    .costEntry(20.0)
                    .build();
            eventRepository.save(event9);
            System.out.println("✓ Evento creado: Taller de Acuarela para Principiantes");

            // Evento 10: Concierto de Música Clásica
            OffsetDateTime start10 = now.plusDays(random.nextInt(10) - 2); // Rango: -2 a +8 días
            Event event10 = Event.builder()
                    .title("Orquesta Sinfónica Nacional")
                    .description("Concierto especial con obras de Beethoven y Mozart.")
                    .location("Teatro Nacional")
                    .startDate(start10)
                    .endDate(start10.plusHours(random.nextInt(3) + 2))
                    .tags(List.of("música", "clásica", "orquesta"))
                    .type(EventType.CONCIERTO)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(600)
                    .imageUrl("https://msi.gob.pe/portal/wp-content/uploads/2016/06/SINFONICA.png")
                    .costEntry(35.0)
                    .build();
            eventRepository.save(event10);
            System.out.println("✓ Evento creado: Orquesta Sinfónica Nacional");

            // Evento 11: Exposición de Fotografía
            OffsetDateTime start11 = now.plusDays(random.nextInt(25) - 10); // Rango: -10 a +15 días
            Event event11 = Event.builder()
                    .title("Miradas del Mundo")
                    .description("Exposición fotográfica de paisajes y culturas del mundo.")
                    .location("Museo de Arte Moderno")
                    .startDate(start11)
                    .endDate(start11.plusDays(random.nextInt(25) + 15))
                    .tags(List.of("fotografía", "exposición", "cultura"))
                    .type(EventType.EXPOSICION)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(150)
                    .imageUrl("https://doihojqqs770p.cloudfront.net/articulos/articulos-63744.jpeg")
                    .costEntry(5.0)
                    .build();
            eventRepository.save(event11);
            System.out.println("✓ Evento creado: Miradas del Mundo");

            // Evento 12: Obra de Teatro Infantil
            OffsetDateTime start12 = now.plusHours(random.nextInt(168) - 48); // Rango: -48 a +120 horas (±5 días)
            Event event12 = Event.builder()
                    .title("El Principito")
                    .description("Adaptación teatral del clásico cuento para toda la familia.")
                    .location("Teatro Infantil")
                    .startDate(start12)
                    .endDate(start12.plusHours(random.nextInt(2) + 1))
                    .tags(List.of("teatro", "infantil", "familia"))
                    .type(EventType.OBRA_DE_TEATRO)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(150)
                    .imageUrl("https://www.gostudent.org/hubfs/Insights_GoStudent_Blog_Images/theatre-kids-opera%20(2).jpg")
                    .costEntry(12.0)
                    .build();
            eventRepository.save(event12);
            System.out.println("✓ Evento creado: El Principito");

            // Evento 13: Conferencia de Marketing Digital
            OffsetDateTime start13 = now.plusDays(random.nextInt(18) - 4); // Rango: -4 a +14 días
            Event event13 = Event.builder()
                    .title("Marketing Digital en la Era Moderna")
                    .description("Estrategias y herramientas para triunfar en el marketing digital.")
                    .location("Hotel Ejecutivo - Sala Conferencias")
                    .startDate(start13)
                    .endDate(start13.plusHours(random.nextInt(6) + 4))
                    .tags(List.of("marketing", "digital", "negocios"))
                    .type(EventType.CONFERENCIA)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(120)
                    .imageUrl("https://www.antevenio.com/wp-content/uploads/2018/05/mejores-eventos-en-Europa-de-Marketing-Digital-2018-destacada.jpg")
                    .costEntry(45.0)
                    .build();
            eventRepository.save(event13);
            System.out.println("✓ Evento creado: Marketing Digital en la Era Moderna");

            // Evento 14: Festival de Comida
            OffsetDateTime start14 = now.plusDays(random.nextInt(12) - 2); // Rango: -2 a +10 días
            Event event14 = Event.builder()
                    .title("Festival Gastronómico Internacional")
                    .description("Degustación de platillos de diversas cocinas del mundo.")
                    .location("Explanada del Parque")
                    .startDate(start14)
                    .endDate(start14.plusDays(random.nextInt(4) + 2))
                    .tags(List.of("gastronomía", "comida", "festival"))
                    .type(EventType.FERIA)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(2000)
                    .imageUrl("https://queenslatino.com/wp-content/uploads/2022/08/Sumaq-aniversario-10-cocineros.jpg")
                    .costEntry(10.0)
                    .build();
            eventRepository.save(event14);
            System.out.println("✓ Evento creado: Festival Gastronómico Internacional");

            // Evento 15: Proyección de Documental
            OffsetDateTime start15 = now.plusHours(random.nextInt(120) - 36); // Rango: -36 a +84 horas
            Event event15 = Event.builder()
                    .title("Documental: Planeta Tierra")
                    .description("Proyección especial del documental sobre la naturaleza.")
                    .location("Cine Cultural")
                    .startDate(start15)
                    .endDate(start15.plusHours(random.nextInt(2) + 1))
                    .tags(List.of("cine", "documental", "naturaleza"))
                    .type(EventType.PROYECCION)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(80)
                    .imageUrl("https://tenemosquever.org.uy/wp-content/uploads/2020/11/LasPionerasNota-1024x512.png")
                    .costEntry(8.0)
                    .build();
            eventRepository.save(event15);
            System.out.println("✓ Evento creado: Documental: Planeta Tierra");

            // Evento 16: Taller de Escritura Creativa
            OffsetDateTime start16 = now.plusDays(random.nextInt(22) - 7); // Rango: -7 a +15 días
            Event event16 = Event.builder()
                    .title("Taller de Narrativa y Cuento Corto")
                    .description("Desarrolla tus habilidades de escritura creativa con escritores reconocidos.")
                    .location("Biblioteca Municipal")
                    .startDate(start16)
                    .endDate(start16.plusHours(random.nextInt(4) + 3))
                    .tags(List.of("escritura", "literatura", "taller"))
                    .type(EventType.TALLER)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(25)
                    .imageUrl("https://letras.unmsm.edu.pe/wp-content/uploads/2019/01/Afiche-taller-escritura-creativa.jpg")
                    .costEntry(18.0)
                    .build();
            eventRepository.save(event16);
            System.out.println("✓ Evento creado: Taller de Narrativa y Cuento Corto");

            // Evento 17: Concierto de Rock Latino
            OffsetDateTime start17 = now.plusDays(random.nextInt(40) - 15); // Rango: -15 a +25 días
            Event event17 = Event.builder()
                    .title("Noche de Rock Latino")
                    .description("Los mejores exponentes del rock en español en un solo escenario.")
                    .location("Estadio Municipal")
                    .startDate(start17)
                    .endDate(start17.plusHours(random.nextInt(4) + 3))
                    .tags(List.of("rock", "música", "latino"))
                    .type(EventType.CONCIERTO)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(5000)
                    .imageUrl("https://www.montrealhispano.com/wp-content/uploads/2020/12/rock-latino-en-netflix-2.jpg")
                    .costEntry(55.0)
                    .build();
            eventRepository.save(event17);
            System.out.println("✓ Evento creado: Noche de Rock Latino");

            // Evento 18: Exposición de Escultura
            OffsetDateTime start18 = now.plusDays(random.nextInt(28) - 8); // Rango: -8 a +20 días
            Event event18 = Event.builder()
                    .title("Formas en el Espacio")
                    .description("Exposición de esculturas contemporáneas de artistas nacionales.")
                    .location("Jardín Botánico")
                    .startDate(start18)
                    .endDate(start18.plusDays(random.nextInt(20) + 10))
                    .tags(List.of("escultura", "arte", "exposición"))
                    .type(EventType.EXPOSICION)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(200)
                    .imageUrl("https://ddclalibertad.gob.pe/wp-content/uploads/2018/04/EXPO-CULTURA-2.jpg")
                    .costEntry(0.0)
                    .build();
            eventRepository.save(event18);
            System.out.println("✓ Evento creado: Formas en el Espacio");

            // Evento 19: Stand-up Comedy
            OffsetDateTime start19 = now.plusHours(random.nextInt(200) - 60); // Rango: -60 a +140 horas
            Event event19 = Event.builder()
                    .title("Noche de Comedia Stand-Up")
                    .description("Los mejores comediantes del país en una noche llena de risas.")
                    .location("Teatro de la Risa")
                    .startDate(start19)
                    .endDate(start19.plusHours(random.nextInt(3) + 1))
                    .tags(List.of("comedia", "humor", "entretenimiento"))
                    .type(EventType.OTRO)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(250)
                    .imageUrl("https://vodevil.pe/wp-content/uploads/2022/03/DSC01152-2000x1333.jpg")
                    .costEntry(22.0)
                    .build();
            eventRepository.save(event19);
            System.out.println("✓ Evento creado: Noche de Comedia Stand-Up");

            // Evento 20: Conferencia de Medio Ambiente
            OffsetDateTime start20 = now.plusDays(random.nextInt(35) - 12); // Rango: -12 a +23 días
            Event event20 = Event.builder()
                    .title("Cumbre Ambiental: Hacia un Futuro Sostenible")
                    .description("Conferencia sobre cambio climático y sostenibilidad ambiental.")
                    .location("Auditorio Universitario")
                    .startDate(start20)
                    .endDate(start20.plusDays(random.nextInt(2) + 1).plusHours(random.nextInt(6)))
                    .tags(List.of("medio ambiente", "sostenibilidad", "conferencia"))
                    .type(EventType.CONFERENCIA)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(400)
                    .imageUrl("https://admin.live.ilo.org/sites/default/files/2023-03/UNDESA.jpg")
                    .costEntry(0.0)
                    .build();
            eventRepository.save(event20);
            System.out.println("✓ Evento creado: Cumbre Ambiental: Hacia un Futuro Sostenible");

            System.out.println("=== Inicialización de eventos completada: 20 eventos creados ===\n");
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