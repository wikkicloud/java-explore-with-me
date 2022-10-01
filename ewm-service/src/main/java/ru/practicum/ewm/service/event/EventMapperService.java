package ru.practicum.ewm.service.event;

import ru.practicum.ewm.dto.event.AdminUpdateEventRequest;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.event.NewEventDto;
import ru.practicum.ewm.dto.event.UpdateEventRequest;
import ru.practicum.ewm.model.event.Event;

import java.util.List;

/**
 * Сервис для работы с DTO взаимодействующий с внешними сервисами
 */
public interface EventMapperService {
    /**
     * Получение DTO
     * @param event событие
     * @return Event
     */
    Event toEvent(NewEventDto event);

    /**
     * Получение DTO
     * @param event событие
     * @return Event
     */
    Event toEvent(UpdateEventRequest event);

    /**
     * Получение DTO
     * @param event событие
     * @return Event
     */
    Event toEvent(AdminUpdateEventRequest event);

    /**
     * Получение DTO
     * @param event событие
     * @return EventFullDto
     */
    EventFullDto toEventFullDto(Event event);

    /**
     * Получение DTO
     * @param event событие
     * @return EventShortDto
     */
    EventShortDto toEventShortDto(Event event);

    /**
     * Список с заданной сортировкой
     * @param events список соьытий
     * @param sort сортировка
     * @return List<EventShortDto>
     */
    List<EventShortDto> toEventShortDtoListBySort(List<Event> events, String sort);
}
