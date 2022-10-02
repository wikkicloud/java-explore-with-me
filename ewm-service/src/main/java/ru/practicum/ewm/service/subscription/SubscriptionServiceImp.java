package ru.practicum.ewm.service.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.NotConditionException;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.request.Request;
import ru.practicum.ewm.model.request.RequestState;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.repository.event.EventRepository;
import ru.practicum.ewm.repository.request.RequestRepository;
import ru.practicum.ewm.repository.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImp implements SubscriptionService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    public User add(int userId, int toUserId) {
        if (userId == toUserId)
            throw new NotConditionException("You can't subscribe to yourself");

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(
                String.format("User id=%s not found", userId)));
        User userToSubscription = userRepository.findById(toUserId).orElseThrow(() -> new NoSuchElementException(
                String.format("User id=%s not found", toUserId)));

        Set<User> subscriptions = user.getUserSubscription();
        if (subscriptions.contains(userToSubscription))
            throw new NotConditionException("You are already subscribed");
        subscriptions.add(userToSubscription);
        user.setUserSubscription(subscriptions);
        return userRepository.save(user);
    }

    @Override
    public User remove(int userId, int toUserId) {
        if (userId == toUserId)
            throw new NotConditionException("you can't get rid of yourself");

        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(
                String.format("User id=%s not found", userId)));
        User userToSubscription = userRepository.findById(toUserId).orElseThrow(() -> new NoSuchElementException(
                String.format("User id=%s not found", toUserId)));
        Set<User> subscriptions = user.getUserSubscription();
        if (!subscriptions.contains(userToSubscription))
            throw new NotConditionException("user is not in subscriptions");
        subscriptions.remove(userToSubscription);
        user.setUserSubscription(subscriptions);
        return userRepository.save(user);
    }

    @Override
    public List<Event> findEvents(int userId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(
                String.format("User id=%s not found", userId)));
        Set<User> subscriptions = user.getUserSubscription();
        return eventRepository.findDistinctByInitiatorIn(subscriptions, PageRequest.of(from / size, size));
    }

    @Override
    public List<Event> findEventsParticipant(int userId, int from, int size) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NoSuchElementException(
                String.format("User id=%s not found", userId)));
        Set<User> subscriptions = user.getUserSubscription();
        List<Request> requests = requestRepository.findByRequesterInAndEvent_EventDateAfterAndState(subscriptions,
                LocalDateTime.now(), RequestState.CONFIRMED, PageRequest.of(from / size, size));
        return requests.stream()
                .map(Request::getEvent)
                .collect(Collectors.toList());
    }
}
