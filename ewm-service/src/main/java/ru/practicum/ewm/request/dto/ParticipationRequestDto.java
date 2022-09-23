package ru.practicum.ewm.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.request.model.RequestState;

import java.time.LocalDateTime;

import static ru.practicum.ewm.util.Constant.TIME_FORMAT;

@Data
@AllArgsConstructor
public class ParticipationRequestDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
    LocalDateTime created;
    int event;
    int id;
    int requester;
    RequestState status;
}
