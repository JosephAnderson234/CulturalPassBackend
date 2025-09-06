package com.culturalpass.culturalpass.User.application;

import com.culturalpass.culturalpass.User.domain.UserService;
import com.culturalpass.culturalpass.User.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
}