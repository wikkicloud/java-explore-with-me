package ru.practicum.ewm.service.request;

import ru.practicum.ewm.model.request.Request;

import java.util.List;

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
}
