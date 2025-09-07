package com.culturalpass.culturalpass.Event.domain;

import com.culturalpass.culturalpass.Event.dto.EventRegistrationTokenResponseDto;
import com.culturalpass.culturalpass.Event.dto.ManualValidationRequestDto;
import com.culturalpass.culturalpass.Event.exceptions.EventNotFoundException;
import com.culturalpass.culturalpass.Event.exceptions.TokenAlreadyValidatedException;
import com.culturalpass.culturalpass.Event.exceptions.TokenNotFoundException;
import com.culturalpass.culturalpass.Event.infrastructure.EventRegistrationTokenRepository;
import com.culturalpass.culturalpass.Event.infrastructure.EventRepository;
import com.culturalpass.culturalpass.Security.exceptions.UserNotFoundException;
import com.culturalpass.culturalpass.User.domain.User;
import com.culturalpass.culturalpass.User.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventRegistrationTokenService {

    @Autowired
    private EventRegistrationTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    public EventRegistrationTokenResponseDto getTokenByUserAndEvent(Long userId, Long eventId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + userId));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado con id: " + eventId));

        EventRegistrationToken token = tokenRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new TokenNotFoundException(
                        "Token no encontrado para usuario " + userId + " y evento " + eventId));

        return mapToResponseDto(token);
    }

    public List<EventRegistrationTokenResponseDto> getTokensByEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado con id: " + eventId));

        List<EventRegistrationToken> tokens = tokenRepository.findByEvent(event);
        return tokens.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public List<EventRegistrationTokenResponseDto> getTokensByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con id: " + userId));

        List<EventRegistrationToken> tokens = tokenRepository.findByUser(user);
        return tokens.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public EventRegistrationTokenResponseDto validateTokenByString(String tokenString) {
        EventRegistrationToken token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new TokenNotFoundException("Token no encontrado: " + tokenString));

        if (token.isValidated()) {
            throw new TokenAlreadyValidatedException("El token ya fue validado anteriormente");
        }

        token.setValidated(true);
        token.setValidatedAt(LocalDateTime.now());

        EventRegistrationToken savedToken = tokenRepository.save(token);
        log.info("Token {} validado para usuario {} en evento {}",
                token.getToken(), token.getUser().getEmail(), token.getEvent().getTitle());

        return mapToResponseDto(savedToken);
    }

    @Transactional
    public EventRegistrationTokenResponseDto validateTokenManually(ManualValidationRequestDto request) {
        User user = userRepository.findByEmailAndFirstName(request.getEmail(), request.getFirstName())
                .orElseThrow(() -> new UserNotFoundException(
                        "Usuario no encontrado con email " + request.getEmail() + " y nombre " + request.getFirstName()));
        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new EventNotFoundException("Evento no encontrado con id: " + request.getEventId()));
        EventRegistrationToken token = tokenRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new TokenNotFoundException(
                        "Token no encontrado para usuario " + user.getEmail() + " en evento " + event.getTitle()));
        if (token.isValidated()) {
            throw new TokenAlreadyValidatedException(
                    "El token para " + user.getEmail() + " en el evento " + event.getTitle() + " ya fue validado anteriormente");
        }
        token.setValidated(true);
        token.setValidatedAt(LocalDateTime.now());
        EventRegistrationToken savedToken = tokenRepository.save(token);
        log.info("Token validado manualmente para usuario {} ({}) en evento {} por admin",
                user.getEmail(), user.getFirstName(), event.getTitle());

        return mapToResponseDto(savedToken);
    }

    public EventRegistrationTokenResponseDto getTokenByString(String tokenString) {
        EventRegistrationToken token = tokenRepository.findByToken(tokenString)
                .orElseThrow(() -> new TokenNotFoundException("Token no encontrado: " + tokenString));

        return mapToResponseDto(token);
    }

    private EventRegistrationTokenResponseDto mapToResponseDto(EventRegistrationToken token) {
        return EventRegistrationTokenResponseDto.builder()
                .id(token.getId())
                .token(token.getToken())
                .userId(token.getUser().getId())
                .userName(token.getUser().getFirstName() + " " + token.getUser().getLastName())
                .userEmail(token.getUser().getEmail())
                .eventId(token.getEvent().getId())
                .eventTitle(token.getEvent().getTitle())
                .eventDate(token.getEvent().getStartDate())
                .validated(token.isValidated())
                .createdAt(token.getCreatedAt())
                .validatedAt(token.getValidatedAt())
                .build();
    }
}