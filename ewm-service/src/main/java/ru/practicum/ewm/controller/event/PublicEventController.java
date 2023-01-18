package ru.practicum.ewm.controller.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.service.event.EventMapperService;
import ru.practicum.ewm.service.event.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.util.Constant.TIME_FORMAT;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Slf4j
public class PublicEventController {
    private final EventService eventService;
    private final EventMapperService eventMapper;

    @GetMapping
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

    @GetMapping("/{id}")
    public EventFullDto getEventFullDto(@PathVariable int id, HttpServletRequest request) {
        log.info("Get event id={}, requestDp={}, requestUri={}", id, request.getRemoteAddr(), request.getRequestURI());
        return eventMapper.toEventFullDto(eventService.getEvent(id, request));
    }
}
