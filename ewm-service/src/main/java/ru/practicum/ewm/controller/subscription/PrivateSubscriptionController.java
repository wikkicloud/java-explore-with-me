package ru.practicum.ewm.controller.subscription;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.user.UserFullDto;
import ru.practicum.ewm.dto.user.UserMapper;
import ru.practicum.ewm.service.event.EventMapperService;
import ru.practicum.ewm.service.subscription.SubscriptionService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users/{userId}/subscriptions")
@RequiredArgsConstructor
@Slf4j
public class PrivateSubscriptionController {
    private final SubscriptionService subscriptionService;
    private final EventMapperService eventMapperService;

    @PostMapping("/{toUserId}")
    public UserFullDto add(@PathVariable int userId, @PathVariable int toUserId) {
        log.info("Add subscription userId={}, toUserId={}", userId, toUserId);
        return UserMapper.toUserFullDto(subscriptionService.add(userId, toUserId));
    }

    @PatchMapping("/{toUserId}/remove")
    public UserFullDto remove(@PathVariable int userId, @PathVariable int toUserId) {
        log.info("Remove subscription userId={}, toUserId={}", userId, toUserId);
        return UserMapper.toUserFullDto(subscriptionService.remove(userId, toUserId));
    }

    @GetMapping
    public List<EventShortDto> findEvents(
            @PathVariable int userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return subscriptionService.findEvents(userId, from, size).stream()
                .map(eventMapperService::toEventShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/participant")
    public List<EventShortDto> findEventsParticipant(
            @PathVariable int userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return subscriptionService.findEventsParticipant(userId, from, size).stream()
                .map(eventMapperService::toEventShortDto)
                .collect(Collectors.toList());
    }
}
