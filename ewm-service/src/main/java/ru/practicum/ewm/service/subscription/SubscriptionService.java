package ru.practicum.ewm.service.subscription;

import org.springframework.data.domain.Page;
import ru.practicum.ewm.model.event.Event;

public interface SubscriptionService {
    /**
     * Подписка на пользователя
     * @param userId id пользователя
     * @param userIdToSubscribing id пользователя на которого подписываемся
     */
    void add(int userId, int userIdToSubscribing);

    /**
     * Удаление подписки на пользователя
     * @param userId id пользователя
     * @param userIdToSubscribing id пользователя от которого отписываемся
     */
    void remove(int userId, int userIdToSubscribing);

    /**
     * События где организатором является пользователь в подписках
     * @param userId id пользователя
     * @param from номер страницы
     * @param size размер страницы
     * @return List<User>
     */
    Page<Event> findEvents(int userId, int from, int size);

    /**
     * События в которых участвует пользователь в подписках
     * @param userId id пользователя
     * @param from номер страницы
     * @param size размер страницы
     * @return List<Event>
     */
    Page<Event> findEventsParticipant(int userId, int from, int size);
}
