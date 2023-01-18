package ru.practicum.ewm.repository.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Integer> {
    /**
     * События по инициатору
     * @param id id инициатора события
     * @param pageable
     * @return List<Event>
     */
    List<Event> findByInitiator_Id(int id, Pageable pageable);

    /**
     * Списко событий
     * @param users список id пользователей
     * @param states список статусов событий
     * @param categories список id категорий
     * @param rangeStart дата начала события
     * @param rangeEnd дата конца события
     * @param pageable
     * @return List<Event>
     */
    List<Event> findByIdInAndStateInAndCategory_IdInAndEventDateAfterAndEventDateBefore(
            List<Integer> users, List<EventState> states, List<Integer> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    /**
     * Список событий
     * @param text строка поиска
     * @param categories категории
     * @param paid оплата
     * @param rangeStart дата начала
     * @param rangeEnd дата конца
     * @param state статус
     * @param pageable
     * @return List<Event>
     */
    @Query("select e from Event e " +
            "where (upper(e.annotation) like upper(concat('%', ?1, '%')) or upper(e.description)" +
            " like upper(concat('%', ?1, '%')))" +
            " and e.category.id in ?2 and e.paid = ?3 and e.eventDate > ?4 and e.eventDate < ?5 and e.state = ?6")
    List<Event> findEvents(String text, List<Integer> categories, boolean paid,
                           LocalDateTime rangeStart, LocalDateTime rangeEnd, EventState state, Pageable pageable);

    /**
     * Список событий
     * @param ids список id событий
     * @returnSet<Event>
     */
    Set<Event> findByIdIn(List<Integer> ids);



}
