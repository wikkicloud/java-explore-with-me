package ru.practicum.ewm.user.service;

import ru.practicum.ewm.user.model.User;

import java.util.List;

public interface UserService {
    User create(User user);

    User get(int id);

    List<User> getUsers(List<Integer> ids, int from, int size);

    User update(int id, User user);

    void remove(int id);
}
