package com.culturalpass.culturalpass.Event.domain;

import com.culturalpass.culturalpass.Event.dto.EventRequestDto;
import com.culturalpass.culturalpass.Event.dto.EventResponseDto;
import com.culturalpass.culturalpass.Event.dto.PaginatedResponseDto;
import com.culturalpass.culturalpass.Event.exceptions.EventAlreadyExistsException;
import com.culturalpass.culturalpass.Event.exceptions.EventNotFoundException;
import com.culturalpass.culturalpass.Event.exceptions.MissingEventFieldException;
import com.culturalpass.culturalpass.Event.infrastructure.EventRepository;
import com.culturalpass.culturalpass.User.dto.UserShortDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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

    public PaginatedResponseDto<EventResponseDto> searchEventsPaginated(
            int page,
            int size,
            String term,
            EventType type,
            String tag,
            String sortByDate,
            String sortByCost) {

        validatePaginationParams(page, size);
        try {
            Specification<Event> spec = createFilterSpecification(term, type, tag);
            Sort sort = createDynamicSort(sortByDate, sortByCost);
            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Event> eventPage = eventRepository.findAll(spec, pageable);
            return buildPaginatedResponse(eventPage);
        } catch (Exception e) {
            throw new MissingEventFieldException("Parámetros de búsqueda inválidos: " + e.getMessage());
        }
    }

    public PaginatedResponseDto<EventResponseDto> getAllEventsPaginated(int page, int size, String sortBy, String sortDir) {
        validatePaginationParams(page, size);
        try {
            Pageable pageable = createPageable(page, size, sortBy, sortDir);
            Page<Event> eventPage = eventRepository.findAll(pageable);
            return buildPaginatedResponse(eventPage);
        } catch (Exception e) {
            throw new MissingEventFieldException("Parámetros de paginación inválidos: " + e.getMessage());
        }
    }

    public PaginatedResponseDto<EventResponseDto> getUsersEvent(Long idUser, int page, int size) {
        validatePaginationParams(page, size);
        Pageable pageable = createPageable(page, size, null, null);
        Page<Event> eventPage = eventRepository.findByRegisteredUsersId(idUser, pageable);
        return buildPaginatedResponse(eventPage);
    }

    public EventResponseDto getEventById(Long id) {
        Event event = findEventById(id);
        return toDto(event);
    }

    public EventResponseDto createEvent(EventRequestDto dto) {
        validateRequiredFields(dto);

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
        Event event = findEventById(id);
        updateEventFromDto(event, dto, false);
        Event updated = eventRepository.save(event);
        return toDto(updated);
    }

    public void deleteEvent(Long id) {
        Event event = findEventById(id);
        eventRepository.delete(event);
    }

    public List<UserShortDto> getUsersByEvent(Long eventId) {
        Event event = findEventById(eventId);
        return event.getRegisteredUsers().stream()
                .map(user -> new UserShortDto(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail()))
                .collect(Collectors.toList());
    }

    public List<EventResponseDto> getNearestAndOpenEvents(List<Event> events) {
        OffsetDateTime now = OffsetDateTime.now();
        return events.stream()
                .filter(event -> (event.getStatus() == EventStatus.APERTURADO ||
                        event.getStatus() == EventStatus.EN_CURSO) &&
                        event.getEndDate() != null && !event.getEndDate().isBefore(now))
                .sorted(Comparator.comparing(Event::getStartDate))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Métodos privados auxiliares

    private void validatePaginationParams(int page, int size) {
        if (page < 0) {
            throw new MissingEventFieldException("El número de página debe ser mayor o igual a 0");
        }
        if (size < 1 || size > 100) {
            throw new MissingEventFieldException("El tamaño de página debe estar entre 1 y 100");
        }
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDir) {
        if (sortBy != null && sortDir != null) {
            Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Sort sort = Sort.by(direction, sortBy);
            return PageRequest.of(page, size, sort);
        }
        return PageRequest.of(page, size);
    }

    private Specification<Event> createFilterSpecification(String term, EventType type, String tag) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro para excluir eventos finalizados (endDate debe ser mayor o igual a la fecha actual)
            OffsetDateTime now = OffsetDateTime.now();
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), now));

            // Filtro por término de búsqueda (título o descripción)
            if (term != null && !term.isBlank()) {
                String searchTerm = "%" + term.toLowerCase() + "%";
                Predicate titlePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")), searchTerm);
                Predicate descriptionPredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("description")), searchTerm);
                predicates.add(criteriaBuilder.or(titlePredicate, descriptionPredicate));
            }

            // Filtro por tipo de evento
            if (type != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), type));
            }

            // Filtro por tag
            if (tag != null && !tag.isBlank()) {
                predicates.add(criteriaBuilder.isMember(tag, root.get("tags")));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private Sort createDynamicSort(String sortByDate, String sortByCost) {
        List<Sort.Order> orders = new ArrayList<>();

        // Ordenar por fecha
        if (sortByDate != null && !sortByDate.isBlank()) {
            if (sortByDate.equalsIgnoreCase("nearest") || sortByDate.equalsIgnoreCase("asc")) {
                orders.add(Sort.Order.asc("startDate"));
            } else if (sortByDate.equalsIgnoreCase("farthest") || sortByDate.equalsIgnoreCase("desc")) {
                orders.add(Sort.Order.desc("startDate"));
            }
        }

        // Ordenar por costo
        if (sortByCost != null && !sortByCost.isBlank()) {
            if (sortByCost.equalsIgnoreCase("cheapest") || sortByCost.equalsIgnoreCase("asc")) {
                orders.add(Sort.Order.asc("costEntry"));
            } else if (sortByCost.equalsIgnoreCase("expensive") || sortByCost.equalsIgnoreCase("desc")) {
                orders.add(Sort.Order.desc("costEntry"));
            }
        }

        // Si no hay ordenamiento especificado, ordenar por fecha ascendente por defecto
        if (orders.isEmpty()) {
            orders.add(Sort.Order.asc("startDate"));
        }

        return Sort.by(orders);
    }

    private PaginatedResponseDto<EventResponseDto> buildPaginatedResponse(Page<Event> eventPage) {
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
    }

    private Event findEventById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado"));
    }

    private void validateRequiredFields(EventRequestDto dto) {
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
        if (dto.getCostEntry() == null) {
            missingFields.append("costEntry, ");
        }
        if (dto.getCapacity() == null) {
            missingFields.append("capacity, ");
        }

        if (!missingFields.isEmpty()) {
            String fields = missingFields.substring(0, missingFields.length() - 2);
            throw new MissingEventFieldException("Faltan campos obligatorios: " + fields);
        }
    }

    public EventResponseDto toDto(Event event) {
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
                event.getTags(),
                event.getCostEntry(),
                event.getCapacity(),
                event.getCurrentEnrollments()
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
        if (isCreate || dto.getCostEntry() != null) event.setCostEntry(dto.getCostEntry());
        if (isCreate || dto.getCapacity() != null) event.setCapacity(dto.getCapacity());
    }
}