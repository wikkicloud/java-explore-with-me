package ru.practicum.ewm.request.service;

import ru.practicum.ewm.request.model.Request;

import java.util.List;

public interface RequestService {
    Request add(int userId, int eventId);

    List<Request> findAll(int userId);

    Request cancelRequest(int requestId, int userId);
}
