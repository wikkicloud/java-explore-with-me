package ru.practicum.ewm.event.service;

import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.request.model.Request;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    Event create(int userId, int catId, Event event);

    List<Event> findEventsIdByUserId(int userId, int from, int size);

    Event update(int userId, int catId, int eventId, Event event);

    Event updateAsAdmin(int eventId, int catId, Event event);

    Event getEvent(int userId, int eventId);

    Event getEvent(int eventId, HttpServletRequest request);

    Event cancelEvent(int userId, int eventId);

    Event publishEvent(int eventId);

    Event rejectEvent(int eventId);

    List<Request> findRequests(int userId, int eventId);

    Request confirmRequest(int userId, int eventId, int reqId);

    Request rejectRequest(int userId, int eventId, int reqId);

    List<Event> getEvents(List<Integer> users, List<EventState> states, List<Integer> categories,
                          LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    List<Event> findEventsPublic(String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd, boolean onlyAvailable, String sort, int from, int size,
                                 HttpServletRequest request);
}
