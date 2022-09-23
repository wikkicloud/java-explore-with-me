package ru.practicum.ewm.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.util.Constant.TIME_FORMAT;

@Data
public class UpdateEventRequest {
    @NotNull
    private int eventId;

    @Size(min = 3, max = 120, message = "Title be between 3 and 120 characters")
    private String title;

    @Size(min = 20, max = 2000, message = "Annotation be between 20 and 2000 characters")
    private String annotation;

    private int category;

    @Size(min = 20, max = 7000, message = "Description be between 20 and 2000 characters")
    private String description;

    @FutureOrPresent(message = "must be a date in the present or in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
    private LocalDateTime eventDate;

    private boolean paid;

    @PositiveOrZero
    private int participantLimit;
}
