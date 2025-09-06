package com.culturalpass.culturalpass.User.domain;

import com.culturalpass.culturalpass.Security.exceptions.UserNotFoundException;
import com.culturalpass.culturalpass.User.dto.UserResponseDto;
import com.culturalpass.culturalpass.User.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDto getMe(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + email));
        return new UserResponseDto(
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole().name(),
                user.getCellphone()
        );
    }
}
