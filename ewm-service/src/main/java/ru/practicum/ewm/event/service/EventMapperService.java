package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.dto.AdminUpdateEventRequest;
import ru.practicum.ewm.event.dto.EventFullDto;
import ru.practicum.ewm.event.dto.EventShortDto;
import ru.practicum.ewm.event.dto.NewEventDto;
import ru.practicum.ewm.event.dto.UpdateEventRequest;
import ru.practicum.ewm.event.model.Event;

import java.util.List;

public interface EventMapperService {
    Event toEvent(NewEventDto event);

    Event toEvent(UpdateEventRequest event);

    Event toEvent(AdminUpdateEventRequest event);

    EventFullDto toEventFullDto(Event event);

    EventShortDto toEventShortDto(Event event);

    List<EventShortDto> toEventShortDtoListBySort(List<Event> events, String sort);
}
