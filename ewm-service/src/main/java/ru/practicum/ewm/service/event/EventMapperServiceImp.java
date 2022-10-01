package ru.practicum.ewm.service.event;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.client.HttpClient;
import ru.practicum.ewm.client.ViewStats;
import ru.practicum.ewm.dto.event.AdminUpdateEventRequest;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventRequest;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.SortType;
import ru.practicum.ewm.dto.user.UserShortDto;

import java.time.LocalDateTime;
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
                .views(getViews(event))
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
                .views(getViews(event))
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

    /**
     * Статистика для собития
     * @param event
     * @return Кол-во просмотров
     */
    private int getViews(Event event) {
        int views = 0;
        String uri = String.format("/events/%s", event.getId());
        List<ViewStats> viewStats = httpClient.getStats(event.getPublished(), LocalDateTime.now(),
                List.of(uri), false);
        if (!viewStats.isEmpty())
            views = viewStats.get(0).getHits();
        return views;
    }
}
