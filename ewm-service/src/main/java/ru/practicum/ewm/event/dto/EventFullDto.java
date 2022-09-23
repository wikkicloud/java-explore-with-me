package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.model.Location;
import ru.practicum.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

import static ru.practicum.ewm.util.Constant.TIME_FORMAT;

@Builder
@Getter
@Setter
@ToString
public class EventFullDto {
    private int id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private int confirmedRequests;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
    private LocalDateTime createdOn;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
    private LocalDateTime eventDate;

    private UserShortDto initiator;
    private Location location;
    private boolean paid;
    private int participantLimit;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
    private LocalDateTime publishedOn;

    private boolean requestModeration;
    private EventState state;
    private int views;
}
