package ru.practicum.ewm.dto.request;

import ru.practicum.ewm.model.request.Request;

public class RequestMapper {
    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(request.getCreated(), request.getEvent().getId(), request.getId(),
                request.getRequester().getId(), request.getState());
    }
}
