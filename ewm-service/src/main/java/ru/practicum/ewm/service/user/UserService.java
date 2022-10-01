package ru.practicum.ewm.service.user;

import ru.practicum.ewm.model.user.User;

import java.util.List;

public interface UserService {
    /**
     * Создание пользователя
     * @param user пользователь
     * @return User
     */
    User create(User user);

    /**
     * Получает пользователя
     * @param id id пользователя
     * @return User
     */
    User get(int id);

    /**
     * Список пользователй
     * @param ids список id пользователей
     * @param from индекс первого элемента
     * @param size размер страницы
     * @return List<User>
     */
    List<User> getUsers(List<Integer> ids, int from, int size);

    /**
     * Обновление пользователя
     * @param id id пользователя
     * @param user пользователь
     * @return User
     */
    User update(int id, User user);

    /**
     * Удаление пользователя
     * @param id id пользователя
     */
    void remove(int id);
}
