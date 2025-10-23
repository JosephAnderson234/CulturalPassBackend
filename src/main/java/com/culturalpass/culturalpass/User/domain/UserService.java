package com.culturalpass.culturalpass.User.domain;

import com.culturalpass.culturalpass.Event.domain.Event;
import com.culturalpass.culturalpass.Event.domain.EventService;
import com.culturalpass.culturalpass.Event.dto.EventResponseDto;
import com.culturalpass.culturalpass.Event.dto.PaginatedResponseDto;
import com.culturalpass.culturalpass.Event.exceptions.EventNotFoundException;
import com.culturalpass.culturalpass.Event.infrastructure.EventRepository;
import com.culturalpass.culturalpass.Event.domain.EventRegistrationToken;
import com.culturalpass.culturalpass.Event.infrastructure.EventRegistrationTokenRepository;
import com.culturalpass.culturalpass.Mail.events.UserEnrolledEvent;
import com.culturalpass.culturalpass.Security.exceptions.UserNotFoundException;
import com.culturalpass.culturalpass.User.dto.UserResponseDto;
import com.culturalpass.culturalpass.User.exceptions.UserAlreadyRegisteredException;
import com.culturalpass.culturalpass.User.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventService eventService;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private EventRegistrationTokenRepository eventRegistrationTokenRepository;

    public UserResponseDto getMe(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + email));
        return new UserResponseDto(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name(),
                user.getCellphone()
        );
    }

    public void registerToEvent(String userEmail, Long eventId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + userEmail));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado con id: " + eventId));

        if (event.getRegisteredUsers().contains(user)) {
            throw new UserAlreadyRegisteredException("El usuario ya está inscrito en este evento.");
        }

        event.getRegisteredUsers().add(user);
        event.setCurrentEnrollments(event.getCurrentEnrollments() + 1);
        eventRepository.save(event);

        String token = java.util.UUID.randomUUID().toString();
        EventRegistrationToken registrationToken = new EventRegistrationToken(token, user, event);
        eventRegistrationTokenRepository.save(registrationToken);
        eventPublisher.publishEvent(new UserEnrolledEvent(this, user, event, token));
    }

    public PaginatedResponseDto<EventResponseDto> getEventsByUsername(String username, int page, int size){
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + username));
        return eventService.getUsersEvent(user.getId(), page, size);
    }

    public boolean isUserEnrolledInEvent(String userEmail, Long eventId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + userEmail));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado con id: " + eventId));

        return event.getRegisteredUsers().contains(user);
    }

    public List<EventResponseDto> getAllEventsByEmail (String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + email));
        List<Event> events = user.getEventsRegistered();
        return events.stream().map(eventService::toDto).toList();
    }

    public List<EventResponseDto> getNearestEventsByUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + email));
        List<Event> events = user.getEventsRegistered();
        return eventService.getNearestAndOpenEvents(events);
    }
}