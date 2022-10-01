package ru.practicum.ewm.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.dto.user.UserMapper;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.service.user.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        User user = userService.create(UserMapper.toUser(userDto));
        log.info("Add user={}", user);
        return UserMapper.toUserDto(user);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable int id) {
        log.info("Remove user id={}", id);
        userService.remove(id);
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable int id) {
        return UserMapper.toUserDto(userService.get(id));
    }

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(required = false) List<Integer> ids,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("Get users ids={}, from={}, size={}", ids, from, size);
        return userService.getUsers(ids, from, size).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
