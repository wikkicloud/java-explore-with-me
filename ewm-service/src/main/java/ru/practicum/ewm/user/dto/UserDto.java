package ru.practicum.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserDto {
    private int id;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Email
    private String email;
}
