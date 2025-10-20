package com.culturalpass.culturalpass;

import com.culturalpass.culturalpass.Event.domain.Event;
import com.culturalpass.culturalpass.Event.domain.EventStatus;
import com.culturalpass.culturalpass.Event.domain.EventType;
import com.culturalpass.culturalpass.Event.infrastructure.EventRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class EventInitializer {
    @Autowired
    private EventRepository eventRepository;

    @PostConstruct
    public void initEvents(){
        if (eventRepository.count() == 0) {
            Event event = Event.builder()
                    .title("Concierto de Rock")
                    .description("Un emocionante concierto de rock con bandas locales.")
                    .location("Auditorio Nacional")
                    .startDate(LocalDate.from(LocalDateTime.of(2024, 9, 15, 20, 0)))
                    .endDate(LocalDate.from(LocalDateTime.of(2024, 9, 15, 20, 0)))
                    .tags(List.of("música", "concierto", "rock"))
                    .type(EventType.CONCIERTO)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(500)
                    .imageUrl("https://example.com/images/concierto_rock.jpg")
                    .costEntry(30.0)
                    .build();
            eventRepository.save(event);
            System.out.println("Evento de prueba 'Concierto de Rock' creado.");
        }
    }
}
