package com.culturalpass.culturalpass.Event.application;

import com.culturalpass.culturalpass.Event.domain.EventService;
import com.culturalpass.culturalpass.Event.domain.ImageService;
import com.culturalpass.culturalpass.Event.dto.EventRequestDto;
import com.culturalpass.culturalpass.Event.dto.EventResponseDto;
import com.culturalpass.culturalpass.Event.domain.EventRegistrationToken;
import com.culturalpass.culturalpass.Event.dto.PaginatedResponseDto;
import com.culturalpass.culturalpass.Event.infrastructure.EventRegistrationTokenRepository;
import com.culturalpass.culturalpass.User.dto.UserShortDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private ImageService imageService;

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    @GetMapping("/paginated")
    public ResponseEntity<PaginatedResponseDto<EventResponseDto>> getAllEventsPaginated(
            @RequestParam(defaultValue = "0") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        PaginatedResponseDto<EventResponseDto> events = eventService.getAllEventsPaginated(currentPage, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/search")
    public ResponseEntity<PaginatedResponseDto<EventResponseDto>> searchEvents(
            @RequestParam(defaultValue = "0") int currentPage,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam String term
    ) {
        return ResponseEntity.ok(eventService.searchEventsPaginated(currentPage, pageSize, term));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> getEvent(@PathVariable Long eventId) {
        return ResponseEntity.ok(eventService.getEventById(eventId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody EventRequestDto dto) {
        return ResponseEntity.ok(eventService.createEvent(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/image/upload/{eventId}")
    public ResponseEntity<String> uploadEventImage(
            @PathVariable Long eventId,
            @RequestParam("imageFile") MultipartFile imageFile) {
        try {
            String url = imageService.uploadEventImage(eventId, imageFile);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{eventId}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable Long eventId, @RequestBody EventRequestDto dto) {
        return ResponseEntity.ok(eventService.updateEvent(eventId, dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{eventId}/participants")
    public ResponseEntity<List<UserShortDto>> getUsersByEvent(@PathVariable Long eventId) {
        List<UserShortDto> users = eventService.getUsersByEvent(eventId);
        return ResponseEntity.ok(users);
    }
}