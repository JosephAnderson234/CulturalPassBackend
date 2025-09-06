package com.culturalpass.culturalpass.User.application;

import com.culturalpass.culturalpass.User.domain.UserService;
import com.culturalpass.culturalpass.User.dto.UserResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}