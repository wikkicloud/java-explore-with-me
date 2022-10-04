package ru.practicum.ewm.service.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.exception.NotConditionException;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventState;
import ru.practicum.ewm.model.request.Request;
import ru.practicum.ewm.model.request.RequestState;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.service.event.EventService;
import ru.practicum.ewm.service.request.RequestService;
import ru.practicum.ewm.service.user.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImp implements SubscriptionService {
    private final UserService userService;
    private final EventService eventService;
    private final RequestService requestService;

    @Override
    public void add(int userId, int userIdToSubscribing) {
        if (userId == userIdToSubscribing) {
            throw new NotConditionException("You can't subscribe to yourself");
        }
        User user = userService.get(userId);
        Set<User> usersInSubscriptions = user.getUserSubscription();
        Set<Integer> userIdsInSubscriptions = getUserIds(usersInSubscriptions);

        if (userIdsInSubscriptions.contains(userIdToSubscribing)) {
            throw new NotConditionException("You are already subscribed");
        }
        User userToSubscription = userService.get(userIdToSubscribing);
        usersInSubscriptions.add(userToSubscription);
        user.setUserSubscription(usersInSubscriptions);
        userService.save(user);
    }

    @Override
    public void remove(int userId, int userIdToSubscribing) {
        if (userId == userIdToSubscribing) {
            throw new NotConditionException("you can't get rid of yourself");
        }
        User user = userService.get(userId);
        Set<User> usersInSubscriptions = user.getUserSubscription();
        Set<Integer> userIdsInSubscriptions = getUserIds(usersInSubscriptions);

        if (!userIdsInSubscriptions.contains(userIdToSubscribing)) {
            throw new NotConditionException("user is not in subscriptions");
        }

        User userToSubscription = userService.get(userIdToSubscribing);
        usersInSubscriptions.remove(userToSubscription);
        user.setUserSubscription(usersInSubscriptions);
        userService.save(user);
    }

    @Override
    public Page<Event> findEvents(int userId, int from, int size) {
        User user = userService.get(userId);
        Set<Integer> userIdsInSubscriptions = getUserIds(user.getUserSubscription());
        return eventService.findEvents(userIdsInSubscriptions, List.of(EventState.PUBLISHED), LocalDateTime.now(),
                PageRequest.of(from, size));
    }

    @Override
    public Page<Event> findEventsParticipant(int userId, int from, int size) {
        User user = userService.get(userId);
        Set<Integer> userIdsInSubscriptions = getUserIds(user.getUserSubscription());
        Page<Request> requests = requestService.findByRequesterIdInAndEventEventDateBeforeAndState(
                userIdsInSubscriptions, LocalDateTime.now(), RequestState.CONFIRMED,
                PageRequest.of(from, size));

        return requests.map(Request::getEvent);
    }

    private Set<Integer> getUserIds(Set<User> users) {
        return users.stream()
                .map(User::getId)
                .collect(Collectors.toSet());
    }
}
