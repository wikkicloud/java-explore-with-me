package ru.practicum.ewm.dto.user;

import ru.practicum.ewm.model.user.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static UserFullDto toUserFullDto(User user) {
        List<UserDto> subs = user.getUserSubscription().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
        return new UserFullDto(user.getId(), user.getName(), user.getEmail(), subs);
    }
}
