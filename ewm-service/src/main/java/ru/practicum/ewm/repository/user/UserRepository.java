package ru.practicum.ewm.repository.user;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Список пользователей
     * @param ids список id пользователей
     * @param pageable
     * @return List<User>
     */
    List<User> findByIdIn(List<Integer> ids, Pageable pageable);

}
