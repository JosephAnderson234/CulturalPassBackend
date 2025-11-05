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
            Event event1 = Event.builder()
                    .title("Concierto de Rock")
                    .description("Un emocionante concierto de rock con bandas locales.")
                    .location("Auditorio Nacional")
                    .startDate(now)
                    .endDate(now.plusHours(random.nextInt(4) + 2))
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
                    .startDate(now)
                    .endDate(now.plusHours(random.nextInt(3) + 1))
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
            Event event3 = Event.builder()
                    .title("Exposición de Arte Moderno")
                    .description("Muestra de arte contemporáneo con obras de artistas emergentes.")
                    .location("Galería de Arte Contemporáneo")
                    .startDate(now)
                    .endDate(now.plusDays(random.nextInt(20) + 10))
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
            Event event4 = Event.builder()
                    .title("Taller de Fotografía Digital")
                    .description("Aprende técnicas básicas y avanzadas de fotografía digital.")
                    .location("Centro Cultural")
                    .startDate(now)
                    .endDate(now.plusHours(random.nextInt(4) + 3))
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
            Event event5 = Event.builder()
                    .title("Festival de Jazz 2025")
                    .description("Festival anual con los mejores músicos de jazz nacional e internacional.")
                    .location("Parque Central")
                    .startDate(now)
                    .endDate(now.plusHours(random.nextInt(6) + 4))
                    .tags(List.of("música", "jazz", "festival"))
                    .type(EventType.CONCIERTO)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(1000)
                    .imageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSQLd6OMp4iXSnr-MgnkrST2q3dVsGtvbh_pw&s")
                    .costEntry(40.0)
                    .build();
            eventRepository.save(event5);
            System.out.println("✓ Evento creado: Festival de Jazz 2025");

            // Evento 6: Cine al Aire Libre
            Event event6 = Event.builder()
                    .title("Ciclo de Cine Clásico")
                    .description("Proyección de películas clásicas bajo las estrellas.")
                    .location("Plaza Mayor")
                    .startDate(now)
                    .endDate(now.plusHours(random.nextInt(2) + 2))
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
            Event event7 = Event.builder()
                    .title("Conferencia Tech Summit 2025")
                    .description("Conferencia sobre las últimas tendencias en tecnología e innovación.")
                    .location("Centro de Convenciones")
                    .startDate(now)
                    .endDate(now.plusDays(random.nextInt(2) + 1).plusHours(random.nextInt(8)))
                    .tags(List.of("tecnología", "innovación", "conferencia"))
                    .type(EventType.CONFERENCIA)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(800)
                    .imageUrl("https://msftstories.thesourcemediaassets.com/sites/41/2023/03/image00016-960x640.jpeg")
                    .costEntry(50.0)
                    .build();
            eventRepository.save(event7);
            System.out.println("✓ Evento creado: Conferencia Tech Summit 2025");

            // Evento 8: Feria de Artesanías
            Event event8 = Event.builder()
                    .title("Feria Nacional de Artesanías")
                    .description("Exposición y venta de artesanías tradicionales de todo el país.")
                    .location("Plaza de Armas")
                    .startDate(now)
                    .endDate(now.plusDays(random.nextInt(5) + 3))
                    .tags(List.of("artesanía", "cultura", "feria"))
                    .type(EventType.FERIA)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(500)
                    .imageUrl("https://www.rumbosdelperu.com/wp-content/uploads/2021/12/ruraq-maki-_10-990x556.jpg")
                    .costEntry(0.0)
                    .build();
            eventRepository.save(event8);
            System.out.println("✓ Evento creado: Feria Nacional de Artesanías");

            // Evento 9: Taller de Pintura
            Event event9 = Event.builder()
                    .title("Taller de Acuarela para Principiantes")
                    .description("Descubre las técnicas básicas de la acuarela en un ambiente relajado.")
                    .location("Escuela de Arte")
                    .startDate(now)
                    .endDate(now.plusHours(random.nextInt(3) + 2))
                    .tags(List.of("arte", "pintura", "taller"))
                    .type(EventType.TALLER)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(20)
                    .imageUrl("https://deshumidificador.mx/wp-content/uploads/2025/05/taller4.webp")
                    .costEntry(20.0)
                    .build();
            eventRepository.save(event9);
            System.out.println("✓ Evento creado: Taller de Acuarela para Principiantes");

            // Evento 10: Concierto de Música Clásica
            Event event10 = Event.builder()
                    .title("Orquesta Sinfónica Nacional")
                    .description("Concierto especial con obras de Beethoven y Mozart.")
                    .location("Teatro Nacional")
                    .startDate(now)
                    .endDate(now.plusHours(random.nextInt(3) + 2))
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
            Event event11 = Event.builder()
                    .title("Miradas del Mundo")
                    .description("Exposición fotográfica de paisajes y culturas del mundo.")
                    .location("Museo de Arte Moderno")
                    .startDate(now)
                    .endDate(now.plusDays(random.nextInt(25) + 15))
                    .tags(List.of("fotografía", "exposición", "cultura"))
                    .type(EventType.EXPOSICION)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(150)
                    .imageUrl("https://i0.wp.com/oscarenfotos.com/wp-content/uploads/2015/04/exhibition.jpg?resize=474%2C269&ssl=1")
                    .costEntry(5.0)
                    .build();
            eventRepository.save(event11);
            System.out.println("✓ Evento creado: Miradas del Mundo");

            // Evento 12: Obra de Teatro Infantil
            Event event12 = Event.builder()
                    .title("El Principito")
                    .description("Adaptación teatral del clásico cuento para toda la familia.")
                    .location("Teatro Infantil")
                    .startDate(now)
                    .endDate(now.plusHours(random.nextInt(2) + 1))
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
            Event event13 = Event.builder()
                    .title("Marketing Digital en la Era Moderna")
                    .description("Estrategias y herramientas para triunfar en el marketing digital.")
                    .location("Hotel Ejecutivo - Sala Conferencias")
                    .startDate(now)
                    .endDate(now.plusHours(random.nextInt(6) + 4))
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
            Event event14 = Event.builder()
                    .title("Festival Gastronómico Internacional")
                    .description("Degustación de platillos de diversas cocinas del mundo.")
                    .location("Explanada del Parque")
                    .startDate(now)
                    .endDate(now.plusDays(random.nextInt(4) + 2))
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
            Event event15 = Event.builder()
                    .title("Documental: Planeta Tierra")
                    .description("Proyección especial del documental sobre la naturaleza.")
                    .location("Cine Cultural")
                    .startDate(now)
                    .endDate(now.plusHours(random.nextInt(2) + 1))
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
            Event event16 = Event.builder()
                    .title("Taller de Narrativa y Cuento Corto")
                    .description("Desarrolla tus habilidades de escritura creativa con escritores reconocidos.")
                    .location("Biblioteca Municipal")
                    .startDate(now)
                    .endDate(now.plusHours(random.nextInt(4) + 3))
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
            Event event17 = Event.builder()
                    .title("Noche de Rock Latino")
                    .description("Los mejores exponentes del rock en español en un solo escenario.")
                    .location("Estadio Municipal")
                    .startDate(now)
                    .endDate(now.plusHours(random.nextInt(4) + 3))
                    .tags(List.of("rock", "música", "latino"))
                    .type(EventType.CONCIERTO)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(5000)
                    .imageUrl("https://static01.nyt.com/images/2020/12/19/arts/16latinrock3-esp-1/merlin_181121733_b145656f-963b-4232-bebf-a62577eca260-articleLarge.jpg?quality=75&auto=webp&disable=upscale")
                    .costEntry(55.0)
                    .build();
            eventRepository.save(event17);
            System.out.println("✓ Evento creado: Noche de Rock Latino");

            // Evento 18: Exposición de Escultura
            Event event18 = Event.builder()
                    .title("Formas en el Espacio")
                    .description("Exposición de esculturas contemporáneas de artistas nacionales.")
                    .location("Jardín Botánico")
                    .startDate(now)
                    .endDate(now.plusDays(random.nextInt(20) + 10))
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
            Event event19 = Event.builder()
                    .title("Noche de Comedia Stand-Up")
                    .description("Los mejores comediantes del país en una noche llena de risas.")
                    .location("Teatro de la Risa")
                    .startDate(now)
                    .endDate(now.plusHours(random.nextInt(3) + 1))
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
            Event event20 = Event.builder()
                    .title("Cumbre Ambiental: Hacia un Futuro Sostenible")
                    .description("Conferencia sobre cambio climático y sostenibilidad ambiental.")
                    .location("Auditorio Universitario")
                    .startDate(now)
                    .endDate(now.plusDays(random.nextInt(2) + 1).plusHours(random.nextInt(6)))
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