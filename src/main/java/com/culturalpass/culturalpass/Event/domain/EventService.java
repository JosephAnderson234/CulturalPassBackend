package com.culturalpass.culturalpass.Event.domain;

import com.culturalpass.culturalpass.Event.dto.EventRequestDto;
import com.culturalpass.culturalpass.Event.dto.EventResponseDto;
import com.culturalpass.culturalpass.Event.dto.PaginatedResponseDto;
import com.culturalpass.culturalpass.Event.exceptions.EventAlreadyExistsException;
import com.culturalpass.culturalpass.Event.exceptions.EventNotFoundException;
import com.culturalpass.culturalpass.Event.exceptions.MissingEventFieldException;
import com.culturalpass.culturalpass.Event.exceptions.UserValidationException;
import com.culturalpass.culturalpass.Event.infrastructure.EventRepository;
import com.culturalpass.culturalpass.User.dto.UserShortDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public PaginatedResponseDto<EventResponseDto> searchEventsPaginated(int page, int size, String term) {
        if (page < 0) {
            throw new UserValidationException("El número de página debe ser mayor o igual a 0");
        }
        if (size < 1 || size > 100) {
            throw new UserValidationException("El tamaño de página debe estar entre 1 y 100");
        }
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Event> eventPage = eventRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(term, term, pageable);
            List<EventResponseDto> content = eventPage.getContent().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

            return PaginatedResponseDto.<EventResponseDto>builder()
                    .content(content)
                    .currentPage(eventPage.getNumber())
                    .totalPages(eventPage.getTotalPages())
                    .totalElements(eventPage.getTotalElements())
                    .size(eventPage.getSize())
                    .build();
        } catch (Exception e) {
            throw new UserValidationException("Parámetros de paginación inválidos: " + e.getMessage());
        }
    }

    public PaginatedResponseDto<EventResponseDto> getAllEventsPaginated(int page, int size, String sortBy, String sortDir) {
        if (page < 0) {
            throw new UserValidationException("El número de página debe ser mayor o igual a 0");
        }
        if (size < 1 || size > 100) {
            throw new UserValidationException("El tamaño de página debe estar entre 1 y 100");
        }
        try {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, sortBy);
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Event> eventPage = eventRepository.findAll(pageable);
            List<EventResponseDto> content = eventPage.getContent().stream()
                    .map(this::toDto)
                    .collect(Collectors.toList());

            return PaginatedResponseDto.<EventResponseDto>builder()
                    .content(content)
                    .currentPage(eventPage.getNumber())
                    .totalPages(eventPage.getTotalPages())
                    .totalElements(eventPage.getTotalElements())
                    .size(eventPage.getSize())
                    .build();
        } catch (Exception e) {
            throw new UserValidationException("Parámetros de paginación inválidos: " + e.getMessage());
        }
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