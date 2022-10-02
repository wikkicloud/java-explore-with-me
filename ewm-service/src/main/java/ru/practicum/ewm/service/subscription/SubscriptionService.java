package ru.practicum.ewm.service.subscription;

import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.user.User;

import java.util.List;

public interface SubscriptionService {
    /**
     * Подписка на пользователя
     * @param userId id пользователя
     * @param toUserId id пользователя на которого подписываемся
     * @return User
     */
    User add(int userId, int toUserId);

    /**
     * Удаление подписки на пользователя
     * @param userId id пользователя
     * @param toUserId id пользователя от которого отписываемся
     * @return User
     */
    User remove(int userId, int toUserId);

    /**
     * События где организатором является пользователь в подписках
     * @param userId id пользователя
     * @param from инедекс первого элемента
     * @param size размер страницы
     * @return List<User>
     */
    List<Event> findEvents(int userId, int from, int size);

    /**
     * События в которых участвует пользователь в подписках
     * @param userId id пользователя
     * @param from инедекс первого элемента
     * @param size размер страницы
     * @return List<Event>
     */
    List<Event> findEventsParticipant(int userId, int from, int size);
}
