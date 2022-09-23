package ru.practicum.ewm.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotConditionException;
import ru.practicum.ewm.request.model.Request;
import ru.practicum.ewm.request.model.RequestState;
import ru.practicum.ewm.request.repository.RequestRepository;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public Request add(int userId, int eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(
                String.format("Event with id=%s was not found.", eventId)));
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(
                String.format("User with id=%s was not found.", userId)));
        if (event.getInitiator().getId() == userId)
            throw new NotConditionException(
                    "The initiator of the event cannot add a request to participate in his event");
        if (event.getState() != EventState.PUBLISHED)
            throw new NotConditionException("You can't participate in an unpublished event");
        if (event.getParticipantLimit() != 0 &&
                requestRepository.countByEvent_IdAndState(eventId, RequestState.CONFIRMED) == event.getParticipantLimit())
            throw new NotConditionException("The event has reached the limit of requests for participation");

        RequestState state;
        if (event.isRequestModeration()) {
            state = RequestState.PENDING;
        } else {
            state = RequestState.CONFIRMED;
        }

        Request request = new Request();
        request.setRequester(user);
        request.setCreated(LocalDateTime.now().withNano(0));
        request.setEvent(event);
        request.setState(state);

        return requestRepository.save(request);
    }

    @Override
    public List<Request> findAll(int userId) {
        return requestRepository.findByRequester_Id(userId);
    }

    @Override
    public Request cancelRequest(int requestId, int userId) {
        Request request = requestRepository.findById(requestId).orElseThrow(() -> new NoSuchElementException(
                String.format("Event with id=%s was not found.", requestId)));
        if (request.getState() == RequestState.CANCELED)
            throw new NotConditionException("You can't cancel an already canceled request");
        request.setState(RequestState.CANCELED);
        return requestRepository.save(request);
    }
}
