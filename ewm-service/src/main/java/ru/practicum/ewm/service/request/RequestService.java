package ru.practicum.ewm.service.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.model.request.Request;
import ru.practicum.ewm.model.request.RequestState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface RequestService {
    /**
     * Добавление запроса на участие в событии
     * @param userId id инициатора
     * @param eventId id события
     * @return Request
     */
    Request add(int userId, int eventId);

    /**
     * Список запросов где requester = userId
     * @param userId id инициатора
     * @return List<Request>
     */
    List<Request> findAll(int userId);

    /**
     * Отмена запроса на участие
     * @param requestId id запроса на участие
     * @param userId id инициатора
     * @return
     */
    Request cancelRequest(int requestId, int userId);

    /**
     * Список запросов
     * @param requesterIds id инициаторов запроса
     * @param eventDate минимальная дата события
     * @param requestState статус запроса на участие
     * @return Page<Request>
     */
    Page<Request> findByRequesterIdInAndEventEventDateBeforeAndState(
            Set<Integer> requesterIds, LocalDateTime eventDate, RequestState requestState, Pageable pageable);
}
