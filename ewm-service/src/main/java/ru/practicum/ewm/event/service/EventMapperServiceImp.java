package ru.practicum.ewm.event.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.client.HttpClient;
import ru.practicum.ewm.event.client.ViewStats;
import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.SortType;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventMapperServiceImp implements EventMapperService {
    private final HttpClient httpClient;

    @Override
    public Event toEvent(NewEventDto event) {
        return Event.builder()
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .location(event.getLocation())
                .requestModeration(event.isRequestModeration())
                .build();
    }

    @Override
    public Event toEvent(UpdateEventRequest event) {
        return Event.builder()
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .build();
    }

    @Override
    public Event toEvent(AdminUpdateEventRequest event) {
        return Event.builder()
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .paid(event.isPaid())
                .participantLimit(event.getParticipantLimit())
                .location(event.getLocation())
                .requestModeration(event.isRequestModeration())
                .build();
    }

    @Override
    public EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .createdOn(event.getCreated())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublished())
                .requestModeration(event.isRequestModeration())
                .state(event.getState())
                .views(getViews(event.getId()))
                .build();
    }

    @Override
    public EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(new CategoryDto(event.getCategory().getId(), event.getCategory().getName()))
                .eventDate(event.getEventDate())
                .initiator(new UserShortDto(event.getInitiator().getId(), event.getInitiator().getName()))
                .paid(event.getPaid())
                .views(getViews(event.getId()))
                .build();
    }

    @Override
    public List<EventShortDto> toEventShortDtoListBySort(List<Event> events, String sort) {
        SortType sortType = SortType.from(sort).orElseThrow(
                () -> new IllegalArgumentException("Unknown state: " + sort));

        switch (sortType) {
            case VIEWS:
                return events.stream()
                        .map(this::toEventShortDto)
                        .sorted(Comparator.comparingInt(EventShortDto::getViews))
                        .collect(Collectors.toList());
            case EVENT_DATE:
                return events.stream()
                        .map(this::toEventShortDto)
                        .sorted(Comparator.comparing(EventShortDto::getEventDate))
                        .collect(Collectors.toList());
            default:
                return events.stream()
                        .map(this::toEventShortDto)
                        .collect(Collectors.toList());
        }
    }

    private int getViews(int id) {
        int views = 0;
        String uri = String.format("/events/%s", id);
        List<ViewStats> viewStats = httpClient.getStats(null, null,
                List.of(uri), false);
        if (!viewStats.isEmpty())
            views = viewStats.get(0).getHits();
        return views;
    }
}
