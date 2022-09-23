package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewm.event.model.Location;

import java.time.LocalDateTime;

import static ru.practicum.ewm.util.Constant.TIME_FORMAT;

@Data
public class AdminUpdateEventRequest {
    private String title;
    private String annotation;
    private int category;
    private String description;
    private Location location;
    private boolean requestModeration = false;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
    private LocalDateTime eventDate;

    private boolean paid;
    private int participantLimit;
}
