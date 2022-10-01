package ru.practicum.ewm.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.AdminUpdateEventRequest;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventState;
import ru.practicum.ewm.service.event.EventMapperService;
import ru.practicum.ewm.service.event.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewm.util.Constant.TIME_FORMAT;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
@Slf4j
public class AdminEventController {

    private final EventService eventService;
    private final EventMapperService eventMapper;

    @GetMapping
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

    @PutMapping("/{eventId}")
    public EventFullDto updateAsAdmin(
            @RequestBody AdminUpdateEventRequest eventRequest,
            @PathVariable int eventId
    ) {
        log.info("Event update admin eventId={}, eventRequest={}", eventId, eventRequest);
        Event event = eventService.updateAsAdmin(eventId, eventRequest.getCategory(),
                eventMapper.toEvent(eventRequest));
        return eventMapper.toEventFullDto(event);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable int eventId) {
        log.info("Publish event id={}", eventId);
        return eventMapper.toEventFullDto(eventService.publishEvent(eventId));
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable int eventId) {
        log.info("Reject event id={}", eventId);
        return eventMapper.toEventFullDto(eventService.rejectEvent(eventId));
    }
}
