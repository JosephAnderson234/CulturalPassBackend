package com.culturalpass.culturalpass.User.application;

import com.culturalpass.culturalpass.Event.dto.EventResponseDto;
import com.culturalpass.culturalpass.Event.dto.PaginatedResponseDto;
import com.culturalpass.culturalpass.User.domain.UserService;
import com.culturalpass.culturalpass.User.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyRole('CLIENTE', 'ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponseDto response = userService.getMe(userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/me/enroll/{eventId}")
    public ResponseEntity<Void> registerToEvent(@AuthenticationPrincipal UserDetails userDetails,
                                                @PathVariable Long eventId) {
        userService.registerToEvent(userDetails.getUsername(), eventId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/events")
    public ResponseEntity<PaginatedResponseDto<EventResponseDto>> getEventsEnrolled(@AuthenticationPrincipal UserDetails userDetails, @RequestParam(defaultValue = "0") int currentPage, @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(userService.getEventsByUsername(userDetails.getUsername(), currentPage, pageSize));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @GetMapping("/events/{eventId}/inrolled")
    public ResponseEntity<Boolean> isUserEnrolledInEvent(@AuthenticationPrincipal UserDetails userDetails,
                                                         @PathVariable Long eventId) {
        Boolean isEnrolled = userService.isUserEnrolledInEvent(userDetails.getUsername(), eventId);
        return ResponseEntity.ok(isEnrolled);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/events/all")
    public ResponseEntity<List<EventResponseDto>> getEventsEnrolledAll(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getAllEventsByEmail(userDetails.getUsername()));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping("/events/nearest")
    public ResponseEntity<List<EventResponseDto>> getNearestEventsWithAperturadoOrEnCursoStatus(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(userService.getNearestEventsByUser(userDetails.getUsername()));
    }

}