package ru.practicum.ewm.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.client.EndpointHit;
import ru.practicum.ewm.event.client.HttpClient;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotConditionException;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestState;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static ru.practicum.ewm.util.Constant.CREATE_HOURS_TO_EVENT;
import static ru.practicum.ewm.util.Constant.PUBLISH_HOURS_TO_EVENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final HttpClient httpClient;

    @Override
    public Event create(int userId, int catId, Event event) {
        Event validEvent = getValidEvent(userId, catId, event);
        validEvent.setState(EventState.PENDING);
        validEvent.setCreated(LocalDateTime.now().withNano(0));
        return eventRepository.save(validEvent);
    }

    @Override
    public List<Event> findEventsIdByUserId(int userId, int from, int size) {
        return eventRepository.findByInitiator_Id(userId, PageRequest.of(from / size, size,
                Sort.by("created").descending()));
    }

    @Override
    public Event update(int userId, int catId, int eventId, Event event) {
        Event eventUpdated = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(
                String.format("Event with id=%s was not found.", eventId)));

        if (eventUpdated.getInitiator().getId() != userId)
            throw new NotConditionException("Only initiator can change event");

        if (eventUpdated.getState().equals(EventState.PUBLISHED))
            throw new NotConditionException("Only pending or canceled events can be changed");

        Event validEvent = getValidEvent(userId, catId, event);

        if (validEvent.getTitle() != null)
            eventUpdated.setTitle(validEvent.getTitle());
        if (validEvent.getAnnotation() != null)
            eventUpdated.setAnnotation(validEvent.getAnnotation());
        if (validEvent.getCategory() != null)
            eventUpdated.setCategory(validEvent.getCategory());
        if (validEvent.getDescription() != null)
            eventUpdated.setDescription(validEvent.getDescription());
        if (validEvent.getEventDate() != null)
            eventUpdated.setEventDate(validEvent.getEventDate());
        if (validEvent.getPaid() != null)
            eventUpdated.setPaid(validEvent.getPaid());
        if (validEvent.getParticipantLimit() != null)
            eventUpdated.setParticipantLimit(validEvent.getParticipantLimit());
        return eventRepository.save(eventUpdated);
    }

    @Override
    public Event updateAsAdmin(int eventId, int catId, Event event) {
        Event eventUpdated = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(
                String.format("Event with id=%s was not found.", eventId)));

        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NoSuchElementException(
                String.format("category with id=%s was not found.", catId)));

        eventUpdated.setCategory(category);
        eventUpdated.setTitle(event.getTitle());
        eventUpdated.setAnnotation(event.getAnnotation());
        eventUpdated.setDescription(event.getDescription());
        eventUpdated.setLocation(event.getLocation());
        eventUpdated.setRequestModeration(event.isRequestModeration());
        eventUpdated.setEventDate(event.getEventDate());
        eventUpdated.setPaid(event.getPaid());
        eventUpdated.setParticipantLimit(event.getParticipantLimit());
        return eventRepository.save(eventUpdated);
    }

    @Override
    public Event getEvent(int userId, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(
                String.format("Event with id=%s was not found.", eventId)));
        if (event.getInitiator().getId() != userId)
            throw new NotConditionException("Access is denied. Only the initiator of the event can get full" +
                    " information about the event");
        return event;
    }

    @Override
    public Event getEvent(int eventId, HttpServletRequest request) {
        sendStat(request);
        return eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(
                String.format("Event with id=%s was not found.", eventId)));

    }

    @Override
    public Event cancelEvent(int userId, int eventId) {
        Event eventUpdated = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(
                String.format("Event with id=%s was not found.", eventId)));
        if (eventUpdated.getInitiator().getId() != userId)
            throw new NotConditionException("Access is denied. Only the initiator of the event can get full" +
                    " information about the event");
        if (eventUpdated.isRequestModeration()) {
            eventUpdated.setState(EventState.CANCELED);
            return eventRepository.save(eventUpdated);
        } else {
            throw new NotConditionException("Only events pending moderation can be canceled");
        }

    }

    @Override
    public Event publishEvent(int eventId) {
        Event eventUpdated = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(
                String.format("Event with id=%s was not found.", eventId)));
        if (!eventUpdated.getState().equals(EventState.PENDING))
            throw new NotConditionException("Only pending events can be published");
        if (eventUpdated.getEventDate().isBefore(LocalDateTime.now().plusHours(PUBLISH_HOURS_TO_EVENT)))
            throw new NotConditionException(String.format("The event will published within %s hours.",
                    PUBLISH_HOURS_TO_EVENT));
        eventUpdated.setState(EventState.PUBLISHED);
        eventUpdated.setPublished(LocalDateTime.now().withNano(0));
        return eventRepository.save(eventUpdated);
    }

    @Override
    public Event rejectEvent(int eventId) {
        Event eventUpdated = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(
                String.format("Event with id=%s was not found.", eventId)));
        if (eventUpdated.getState().equals(EventState.PUBLISHED))
            throw new NotConditionException("Only pending events can be rejected");
        eventUpdated.setState(EventState.CANCELED);
        return eventRepository.save(eventUpdated);
    }

    @Override
    public List<Request> findRequests(int userId, int eventId) {
        eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(
                String.format("Event with id=%s was not found.", eventId)));
        userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(
                String.format("User with id=%s was not found.", userId)));
        return requestRepository.findByEvent_IdAndEvent_Initiator_Id(eventId, userId);
    }

    @Override
    public Request confirmRequest(int userId, int eventId, int reqId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(
                String.format("Event with id=%s was not found.", eventId)));
        userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(
                String.format("User with id=%s was not found.", userId)));
        Request requestToUpdated = requestRepository.findById(reqId).orElseThrow(() -> new NoSuchElementException(
                String.format("Request with id=%s was not found.", reqId)));

        if (!event.isRequestModeration() || event.getParticipantLimit() == 0)
            throw new NotConditionException(
                    "For an event, the limit of applications is 0 or pre-moderation of applications is disabled");
        long countConfirmedEventRequests = requestRepository.countByEvent_IdAndState(eventId, RequestState.CONFIRMED);
        if (event.getParticipantLimit() == countConfirmedEventRequests)
            throw new NotConditionException(
                    "The limit on applications for this event has been reached");

        //Последняя заявка отклоняем остальные
        if (event.getParticipantLimit() - countConfirmedEventRequests == 1) {
            requestRepository.findByEvent_IdAndState(eventId, RequestState.PENDING)
                    .forEach(request -> {
                        request.setState(RequestState.REJECTED);
                        requestRepository.save(request);
                    });
        }
        requestToUpdated.setState(RequestState.CONFIRMED);
        return requestRepository.save(requestToUpdated);
    }

    @Override
    public Request rejectRequest(int userId, int eventId, int reqId) {
        eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(
                String.format("Event with id=%s was not found.", eventId)));
        userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(
                String.format("User with id=%s was not found.", userId)));
        Request requestToUpdated = requestRepository.findById(reqId).orElseThrow(() -> new NoSuchElementException(
                String.format("Request with id=%s was not found.", reqId)));
        requestToUpdated.setState(RequestState.REJECTED);
        return requestRepository.save(requestToUpdated);
    }

    @Override
    public List<Event> getEvents(
            List<Integer> users, List<EventState> states, List<Integer> categories,
            LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size
    ) {
        return eventRepository.findByIdInAndStateInAndCategory_IdInAndEventDateAfterAndEventDateBefore(
                users, states, categories, rangeStart, rangeEnd, PageRequest.of(from / size, size)
        );
    }

    @Override
    public List<Event> findEventsPublic(
            String text, List<Integer> categories, boolean paid, LocalDateTime rangeStart, LocalDateTime rangeEnd,
            boolean onlyAvailable, String sort, int from, int size, HttpServletRequest request
    ) {
        sendStat(request);
        return eventRepository.findEvents(text, categories, paid, rangeStart, rangeEnd,
                EventState.PUBLISHED, PageRequest.of(from / size, size));
    }


    private Event getValidEvent(int userId, int catId, Event event) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(
                String.format("user with id=%s was not found.", userId)));
        event.setInitiator(user);
        Category category = categoryRepository.findById(catId).orElseThrow(() -> new NoSuchElementException(
                String.format("category with id=%s was not found.", catId)));
        event.setCategory(category);

        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(CREATE_HOURS_TO_EVENT)))
            throw new NotConditionException(String.format("The event will start within %s hours.",
                    CREATE_HOURS_TO_EVENT));
        return event;
    }

    private void sendStat(HttpServletRequest request) {
        httpClient.setHit("/hit", EndpointHit.builder()
                .ip(request.getRemoteAddr())
                .app("ewm-service")
                .uri(request.getRequestURI())
                .timestamp(LocalDateTime.now().withNano(0))
                .build()
        );
    }

}
