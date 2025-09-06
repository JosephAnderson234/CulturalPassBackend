package com.culturalpass.culturalpass.User.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String cellphone;
}