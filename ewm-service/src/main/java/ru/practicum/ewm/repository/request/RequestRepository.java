package ru.practicum.ewm.repository.request;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.request.Request;
import ru.practicum.ewm.model.request.RequestState;
import ru.practicum.ewm.model.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    /**
     * Кол-во запросов для участия в событии
     * @param id id события
     * @param state статус события
     * @return long
     */
    long countByEvent_IdAndState(int id, RequestState state);

    /**
     * Список запрсов для участия в событии
     * @param id id инициатора запроса
     * @return List<Request>
     */
    List<Request> findByRequester_Id(int id);

    /**
     * Список запросов запрсов для участия в событии
     * @param eventId id события
     * @param userId id инициатора события
     * @return List<Request>
     */
    List<Request> findByEvent_IdAndEvent_Initiator_Id(int eventId, int userId);

    /**
     * Список запросов запрсов для участия в событии
     * @param id id события
     * @param state статус запроса
     * @return List<Request>
     */
    List<Request> findByEvent_IdAndState(int id, RequestState state);

    /**
     * Список запросов
     * @param requesters список инициаторов запроса на участие в событии
     * @param eventDate Дата начала события
     * @param state стутус запроса на участие
     * @return List<Request>
     */
    List<Request> findByRequesterInAndEvent_EventDateAfterAndState(Set<User> requesters, LocalDateTime eventDate,
                                                                   RequestState state, Pageable pageable);


}
