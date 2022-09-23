package ru.practicum.ewm.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByInitiator_Id(int id, Pageable pageable);

    List<Event> findByIdInAndStateInAndCategory_IdInAndEventDateAfterAndEventDateBefore(
            List<Integer> users, List<EventState> states, List<Integer> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event e " +
            "where (upper(e.annotation) like upper(concat('%', ?1, '%')) or upper(e.description)" +
            " like upper(concat('%', ?1, '%')))" +
            " and e.category.id in ?2 and e.paid = ?3 and e.eventDate > ?4 and e.eventDate < ?5 and e.state = ?6")
    List<Event> findEvents(String text, List<Integer> categories, boolean paid,
                           LocalDateTime rangeStart, LocalDateTime rangeEnd, EventState state, Pageable pageable);

    Set<Event> findByIdIn(List<Integer> ids);



}
