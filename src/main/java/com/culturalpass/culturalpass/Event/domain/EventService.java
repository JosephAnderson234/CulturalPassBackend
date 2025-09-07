package com.culturalpass.culturalpass.Event.domain;

import com.culturalpass.culturalpass.Event.dto.EventRequestDto;
import com.culturalpass.culturalpass.Event.dto.EventResponseDto;
import com.culturalpass.culturalpass.Event.exceptions.EventAlreadyExistsException;
import com.culturalpass.culturalpass.Event.exceptions.EventNotFoundException;
import com.culturalpass.culturalpass.Event.exceptions.MissingEventFieldException;
import com.culturalpass.culturalpass.Event.infrastructure.EventRepository;
import com.culturalpass.culturalpass.User.dto.UserShortDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<EventResponseDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public EventResponseDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado"));
        return toDto(event);
    }

    public EventResponseDto createEvent(EventRequestDto dto) {
        StringBuilder missingFields = new StringBuilder();

        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            missingFields.append("title, ");
        }
        if (dto.getDescription() == null || dto.getDescription().isBlank()) {
            missingFields.append("description, ");
        }
        if (dto.getStartDate() == null) {
            missingFields.append("startDate, ");
        }
        if (dto.getEndDate() == null) {
            missingFields.append("endDate, ");
        }
        if (dto.getLocation() == null || dto.getLocation().isBlank()) {
            missingFields.append("location, ");
        }
        if (dto.getType() == null) {
            missingFields.append("type, ");
        }
        if (dto.getStatus() == null) {
            missingFields.append("status, ");
        }

        if (missingFields.length() > 0) {
            String fields = missingFields.substring(0, missingFields.length() - 2);
            throw new MissingEventFieldException("Faltan campos obligatorios: " + fields);
        }

        if (eventRepository.existsByTitle(dto.getTitle())) {
            throw new EventAlreadyExistsException("Ya existe un evento con el título: " + dto.getTitle());
        }

        Event event = new Event();
        updateEventFromDto(event, dto, true);
        event.setRegisteredUsers(List.of());
        Event saved = eventRepository.save(event);
        return toDto(saved);
    }

    public EventResponseDto updateEvent(Long id, EventRequestDto dto) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado"));
        updateEventFromDto(event, dto, false);
        Event updated = eventRepository.save(event);
        return toDto(updated);
    }

    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado"));
        eventRepository.delete(event);
    }

    public List<UserShortDto> getUsersByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado"));
        return event.getRegisteredUsers().stream()
                .map(user -> new UserShortDto(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail()))
                .collect(Collectors.toList());
    }

    private EventResponseDto toDto(Event event) {
        return new EventResponseDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getImageUrl(),
                event.getStartDate(),
                event.getEndDate(),
                event.getLocation(),
                event.getType(),
                event.getStatus(),
                event.getTags()
        );
    }

    private void updateEventFromDto(Event event, EventRequestDto dto, boolean isCreate) {
        if (isCreate || dto.getTitle() != null) event.setTitle(dto.getTitle());
        if (isCreate || dto.getDescription() != null) event.setDescription(dto.getDescription());
        if (isCreate || dto.getImageUrl() != null) event.setImageUrl(dto.getImageUrl());
        if (isCreate || dto.getStartDate() != null) event.setStartDate(dto.getStartDate());
        if (isCreate || dto.getEndDate() != null) event.setEndDate(dto.getEndDate());
        if (isCreate || dto.getLocation() != null) event.setLocation(dto.getLocation());
        if (isCreate || dto.getType() != null) event.setType(dto.getType());
        if (isCreate || dto.getStatus() != null) event.setStatus(dto.getStatus());
        if (isCreate || dto.getTags() != null) event.setTags(dto.getTags());
    }
}