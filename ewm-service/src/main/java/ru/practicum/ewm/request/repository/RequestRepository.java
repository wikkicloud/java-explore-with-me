package ru.practicum.ewm.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestState;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    long countByEvent_IdAndState(int id, RequestState state);

    List<Request> findByRequester_Id(int id);

    List<Request> findByEvent_IdAndEvent_Initiator_Id(int eventId, int userId);

    List<Request> findByEvent_IdAndState(int id, RequestState state);
}
