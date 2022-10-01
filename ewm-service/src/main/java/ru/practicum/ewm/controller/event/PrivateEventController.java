package ru.practicum.ewm.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventRequest;
import ru.practicum.ewm.dto.request.ParticipationRequestDto;
import ru.practicum.ewm.dto.request.RequestMapper;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.service.event.EventMapperService;
import ru.practicum.ewm.service.event.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
@Slf4j
public class PrivateEventController {
    private final EventService eventService;
    private final EventMapperService eventMapper;

    @PostMapping
    public EventFullDto create(
            @PathVariable int userId,
            @Valid @RequestBody NewEventDto eventDto) {
        log.info("Add event userId={}, NewEventDto={}", userId, eventDto);
        Event event = eventService.create(userId, eventDto.getCategory(), eventMapper.toEvent(eventDto));
        return eventMapper.toEventFullDto(event);
    }

    @PatchMapping
    public EventFullDto update(
            @Valid @PathVariable int userId,
            @Valid @RequestBody UpdateEventRequest eventRequest) {
        log.info("Update event userId={}, NewEventDto={}", userId, eventRequest);
        Event event = eventService.update(userId, eventRequest.getCategory(), eventRequest.getEventId(),
                eventMapper.toEvent(eventRequest));
        return eventMapper.toEventFullDto(event);
    }

    @GetMapping
    public List<EventShortDto> findEventsIdByUserId(
            @PathVariable int userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("Find Events by userId={}", userId);
        return eventService.findEventsIdByUserId(userId, from, size).stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable int userId, @PathVariable int eventId) {
        log.info("Get EventFullDto userId={}, evenId={}", userId, eventId);
        Event event = eventService.getEvent(userId, eventId);
        return eventMapper.toEventFullDto(event);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(
            @PathVariable int userId,
            @PathVariable int eventId
    ) {
        log.info("Get EventFullDto userId={}, evenId={}", userId, eventId);
        Event event = eventService.cancelEvent(userId, eventId);
        return eventMapper.toEventFullDto(event);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> findRequests(@PathVariable int userId, @PathVariable int eventId) {
        log.info("Get requests userId={}, eventId={}", userId, eventId);
        return eventService.findRequests(userId, eventId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable int userId, @PathVariable int eventId,
                                                  @PathVariable int reqId) {
        log.info("Confirm request userId={}, eventId={}, reqId={}", userId, eventId, reqId);
        return RequestMapper.toParticipationRequestDto(eventService.confirmRequest(userId, eventId, reqId));
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable int userId, @PathVariable int eventId,
                                                 @PathVariable int reqId) {
        log.info("Reject request userId={}, eventId={}, reqId={}", userId, eventId, reqId);
        return RequestMapper.toParticipationRequestDto(eventService.rejectRequest(userId, eventId, reqId));
    }
}
