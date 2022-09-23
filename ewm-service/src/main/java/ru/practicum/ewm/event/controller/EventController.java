package ru.practicum.ewm.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.service.EventMapperService;
import ru.practicum.ewm.event.service.EventService;
import ru.practicum.ewm.request.dto.ParticipationRequestDto;
import ru.practicum.ewm.request.dto.RequestMapper;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.util.Constant.TIME_FORMAT;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EventController {
    private final EventService eventService;
    private final EventMapperService eventMapper;

    /*Admin permission*/
    @GetMapping("/admin/events")
    public List<EventFullDto> getEvents(
            @RequestParam List<Integer> users,
            @RequestParam List<EventState> states,
            @RequestParam List<Integer> categories,
            @DateTimeFormat(pattern = TIME_FORMAT) @RequestParam LocalDateTime rangeStart,
            @DateTimeFormat(pattern = TIME_FORMAT) @RequestParam LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("Get events users={}, states={}, categories={}, rangeStart={}, rangeEnd={}, from={}, size{}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size).stream()
                .map(eventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    @PutMapping("/admin/events/{eventId}")
    public EventFullDto updateAsAdmin(
            @RequestBody AdminUpdateEventRequest eventRequest,
            @PathVariable int eventId
    ) {
        log.info("Event update admin eventId={}, eventRequest={}", eventId, eventRequest);
        Event event = eventService.updateAsAdmin(eventId, eventRequest.getCategory(),
                eventMapper.toEvent(eventRequest));
        return eventMapper.toEventFullDto(event);
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable int eventId) {
        log.info("Publish event id={}", eventId);
        return eventMapper.toEventFullDto(eventService.publishEvent(eventId));
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable int eventId) {
        log.info("Reject event id={}", eventId);
        return eventMapper.toEventFullDto(eventService.rejectEvent(eventId));
    }

    /*User permission*/
    @PostMapping("/users/{userId}/events")
    public EventFullDto create(
            @PathVariable int userId,
            @Valid @RequestBody NewEventDto eventDto) {
        log.info("Add event userId={}, NewEventDto={}", userId, eventDto);
        Event event = eventService.create(userId, eventDto.getCategory(), eventMapper.toEvent(eventDto));
        return eventMapper.toEventFullDto(event);
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto update(
            @Valid @PathVariable int userId,
            @Valid @RequestBody UpdateEventRequest eventRequest) {
        log.info("Update event userId={}, NewEventDto={}", userId, eventRequest);
        Event event = eventService.update(userId, eventRequest.getCategory(), eventRequest.getEventId(),
                eventMapper.toEvent(eventRequest));
        return eventMapper.toEventFullDto(event);
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> findEventsIdByUserId(
            @PathVariable int userId,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("Find Events id by userId={}", userId);
        return eventService.findEventsIdByUserId(userId, from, size).stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable int userId, @PathVariable int eventId) {
        log.info("Get EventFullDto userId={}, evenId={}", userId, eventId);
        Event event = eventService.getEvent(userId, eventId);
        return eventMapper.toEventFullDto(event);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancelEvent(
            @PathVariable int userId,
            @PathVariable int eventId
    ) {
        log.info("Get EventFullDto userId={}, evenId={}", userId, eventId);
        Event event = eventService.cancelEvent(userId, eventId);
        return eventMapper.toEventFullDto(event);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findRequests(@PathVariable int userId, @PathVariable int eventId) {
        log.info("Get requests userId={}, eventId={}", userId, eventId);
        return eventService.findRequests(userId, eventId).stream()
                .map(RequestMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable int userId, @PathVariable int eventId,
                                                  @PathVariable int reqId) {
        log.info("Confirm request userId={}, eventId={}, reqId={}", userId, eventId, reqId);
        return RequestMapper.toParticipationRequestDto(eventService.confirmRequest(userId, eventId, reqId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable int userId, @PathVariable int eventId,
                                                  @PathVariable int reqId) {
        log.info("Reject request userId={}, eventId={}, reqId={}", userId, eventId, reqId);
        return RequestMapper.toParticipationRequestDto(eventService.rejectRequest(userId, eventId, reqId));
    }

    /*Public permission*/
    @GetMapping("/events")
    public List<EventShortDto> findEvents(
            @RequestParam String text,
            @RequestParam List<Integer> categories,
            @RequestParam boolean paid,
            @DateTimeFormat(pattern = TIME_FORMAT) @RequestParam LocalDateTime rangeStart,
            @DateTimeFormat(pattern = TIME_FORMAT) @RequestParam LocalDateTime rangeEnd,
            @RequestParam boolean onlyAvailable,
            @RequestParam(name = "sort", defaultValue = "EVENT_DATE") String sort,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        List<Event> events = eventService.findEventsPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size, request);
        return eventMapper.toEventShortDtoListBySort(events, sort);
    }

    @GetMapping("events/{id}")
    public EventFullDto getEventFullDto(@PathVariable int id, HttpServletRequest request) {
        log.info("Get event id={}, requestDp={}, requestUri={}", id, request.getRemoteAddr(), request.getRequestURI());
        return eventMapper.toEventFullDto(eventService.getEvent(id, request));
    }
}
