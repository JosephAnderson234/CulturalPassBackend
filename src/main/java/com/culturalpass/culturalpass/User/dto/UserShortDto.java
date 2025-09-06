package com.culturalpass.culturalpass.User.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    private String firstName;
    private String lastName;
    private String email;
}