package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewm.model.event.Location;

import java.time.LocalDateTime;

import static ru.practicum.ewm.util.Constant.TIME_FORMAT;

/**
 * DTO обновления события администратором
 */
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
