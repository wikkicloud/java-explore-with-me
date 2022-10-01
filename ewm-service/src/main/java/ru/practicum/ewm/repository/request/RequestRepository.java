package ru.practicum.ewm.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.request.Request;
import ru.practicum.ewm.model.request.RequestState;

import java.util.List;

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
     * @return
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
     * @return
     */
    List<Request> findByEvent_IdAndState(int id, RequestState state);
}
