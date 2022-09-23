package ru.practicum.ewm.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestMapper;
import ru.practicum.ewm.request.service.RequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
@Slf4j
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public ParticipationRequestDto add(@PathVariable int userId, @RequestParam int eventId) {
        log.info("Add request userId={}, eventId={}", userId, eventId);
        return RequestMapper.toParticipationRequestDto(requestService.add(userId, eventId));
    }

    @GetMapping
    public List<ParticipationRequestDto> findAll(@PathVariable int userId) {
        log.info("Get requests userId={}", userId);
        return requestService.findAll(userId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable int userId, @PathVariable int requestId) {
        log.info("Canceled requestId={}, userId={}", requestId, userId);
        return RequestMapper.toParticipationRequestDto(requestService.cancelRequest(requestId, userId));
    }
}
