package com.culturalpass.culturalpass.Event.application;

import com.culturalpass.culturalpass.Event.domain.EventRegistrationTokenService;
import com.culturalpass.culturalpass.Event.dto.EventRegistrationTokenResponseDto;
import com.culturalpass.culturalpass.Event.dto.ManualValidationRequestDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/token")
public class EventRegistrationTokenController {

    @Autowired
    private EventRegistrationTokenService tokenService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}/event/{eventId}")
    public ResponseEntity<EventRegistrationTokenResponseDto> getTokenByUserAndEvent(
            @PathVariable Long userId,
            @PathVariable Long eventId) {
        EventRegistrationTokenResponseDto token = tokenService.getTokenByUserAndEvent(userId, eventId);
        return ResponseEntity.ok(token);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/user/me/event/{eventId}")
    public ResponseEntity<EventRegistrationTokenResponseDto> getMyTokenByEvent(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long eventId) {
        EventRegistrationTokenResponseDto token = tokenService.getTokenByUserEmailAndEvent(
                userDetails.getUsername(),
                eventId
        );
        return ResponseEntity.ok(token);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<EventRegistrationTokenResponseDto>> getTokensByEvent(@PathVariable Long eventId) {
        List<EventRegistrationTokenResponseDto> tokens = tokenService.getTokensByEvent(eventId);
        return ResponseEntity.ok(tokens);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<EventRegistrationTokenResponseDto>> getTokensByUser(@PathVariable Long userId) {
        List<EventRegistrationTokenResponseDto> tokens = tokenService.getTokensByUser(userId);
        return ResponseEntity.ok(tokens);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/validate/{tokenString}")
    public ResponseEntity<EventRegistrationTokenResponseDto> validateToken(@PathVariable String tokenString) {
        EventRegistrationTokenResponseDto token = tokenService.validateTokenByString(tokenString);
        return ResponseEntity.ok(token);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/info/{tokenString}")
    public ResponseEntity<EventRegistrationTokenResponseDto> getTokenInfo(@PathVariable String tokenString) {
        EventRegistrationTokenResponseDto token = tokenService.getTokenByString(tokenString);
        return ResponseEntity.ok(token);
    }

    // VALIDACION MANUAL - (Caso donde no haya recibido el qr por correo)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/validate-manual")
    public ResponseEntity<EventRegistrationTokenResponseDto> validateTokenManually(
            @Valid @RequestBody ManualValidationRequestDto request) {
        EventRegistrationTokenResponseDto token = tokenService.validateTokenManually(request);
        return ResponseEntity.ok(token);
    }
}
