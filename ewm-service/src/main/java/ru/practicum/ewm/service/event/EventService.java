package ru.practicum.ewm.service.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventState;
import ru.practicum.ewm.model.request.Request;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventService {
    /**
     * Создание события
     * @param userId id пользователя
     * @param catId id категории
     * @param event событие
     * @return Event
     */
    Event create(int userId, int catId, Event event);

    /**
     * Список событий где инициатор userId
     * @param userId id пользователя
     * @param from индекс первого элемента
     * @param size размер страницы
     * @return List<Event>
     */
    List<Event> findEventsIdByUserId(int userId, int from, int size);

    /**
     * Обновление события инициатором
     * @param userId id пользователя
     * @param catId id категории
     * @param eventId id события
     * @param event событие
     * @return Event
     */
    Event update(int userId, int catId, int eventId, Event event);

    /**
     * Обновление события администратором
     * @param eventId id события
     * @param catId id категории
     * @param event событие
     * @return Event
     */
    Event updateAsAdmin(int eventId, int catId, Event event);

    /**
     * Получение события инициатором
     * @param userId id пользователя
     * @param eventId id события
     * @return Event
     */
    Event getEvent(int userId, int eventId);

    /**
     * Получение события для всех, с регистрацией статистики
     * @param eventId id события
     * @param request запрос на участие
     * @return Event
     */
    Event getEvent(int eventId, HttpServletRequest request);

    /**
     * Отмена события пользователем
     * @param userId id ползователя
     * @param eventId id события
     * @return Event
     */
    Event cancelEvent(int userId, int eventId);

    /**
     * Публикация события
     * @param eventId id события
     * @return Event
     */
    Event publishEvent(int eventId);

    /**
     * Отклонение события
     * @param eventId id события
     * @return Event
     */
    Event rejectEvent(int eventId);

    /**
     * Запросы на участие в событии где userId инициатор
     * @param userId id пользователя
     * @param eventId id события
     * @return List<Request>
     */
    List<Request> findRequests(int userId, int eventId);

    /**
     * Подтверждение запроса на участие
     * @param userId id пользователя
     * @param eventId id события
     * @param reqId id запроса на участие
     * @return Request
     */
    Request confirmRequest(int userId, int eventId, int reqId);

    /**
     * Отклонение запроса на участие
     * @param userId id пользователя
     * @param eventId id события
     * @param reqId d запроса на участие
     * @return Request
     */
    Request rejectRequest(int userId, int eventId, int reqId);

    /**
     * Получение событий администратором
     * @param users список id инициаторов
     * @param states список статусов событий
     * @param categories список категорий событий
     * @param rangeStart начало промежутка начала события
     * @param rangeEnd конец промежутка начала события
     * @param from индекс первого элемента
     * @param size размер страницы
     * @return List<Event>
     */
    List<Event> getEvents(List<Integer> users, List<EventState> states, List<Integer> categories,
                          LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    /**
     * Получение событий пользователями
     * @param users список id инициаторов
     * @param states список статусов событий
     * @param rangeStart начало промежутка начала события
     * @param from индекс первого элемента
     * @param size размер страницы
     * @return List<Event>
     */
    Page<Event> findEvents(Set<Integer> userIds, List<EventState> states, LocalDateTime rangeStart, Pageable pageable);

    /**
     * Получение событий публичный доступ
     * @param text текст для поиска по анотации и описанию
     * @param categories список id категорий
     * @param paid true - требуется оплата, false - не требуется
     * @param rangeStart начало промежутка начала события
     * @param rangeEnd конец промежутка начала события
     * @param onlyAvailable  доступность
     * @param sort сортировка
     * @param from индекс первого элемента
     * @param size размер страницы
     * @param request запрос переданный из контроллера
     * @return List<Event>
     */
    List<Event> findEventsPublic(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size,
                                 HttpServletRequest request);
}
