package com.culturalpass.culturalpass;

import com.culturalpass.culturalpass.Event.domain.Event;
import com.culturalpass.culturalpass.Event.domain.EventStatus;
import com.culturalpass.culturalpass.Event.domain.EventType;
import com.culturalpass.culturalpass.Event.infrastructure.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventInitializer {
    @Autowired
    private EventRepository eventRepository;

    @EventListener(org.springframework.boot.context.event.ApplicationReadyEvent.class)
    public void initEvents() {
        if (eventRepository.count() == 0) {
            Event event = Event.builder()
                    .title("Concierto de Rock")
                    .description("Un emocionante concierto de rock con bandas locales.")
                    .location("Auditorio Nacional")
                    .startDate(java.time.LocalDateTime.of(2025, 11, 22, 20, 0, 0))
                    .endDate(java.time.LocalDateTime.of(2025, 11, 22, 23, 0, 0))
                    .tags(List.of("música", "concierto", "rock"))
                    .type(EventType.CONCIERTO)
                    .status(EventStatus.APERTURADO)
                    .currentEnrollments(0)
                    .capacity(500)
                    .imageUrl("https://imgmedia.larepublica.pe/1000x590/larepublica/original/2025/10/17/68f30686b3f6c319b802ec28.webp")
                    .costEntry(30.0)
                    .build();
            eventRepository.save(event);
            System.out.println("Evento de prueba 'Concierto de Rock' creado.");
        }
    }
}
