package ru.practicum.ewm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserFullDto {
    private int id;
    private String name;
    private String email;
    private List<UserDto> subscriptions;
}
