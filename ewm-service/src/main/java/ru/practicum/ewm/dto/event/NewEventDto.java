package ru.practicum.ewm.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewm.model.event.Location;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static ru.practicum.ewm.util.Constant.TIME_FORMAT;

/**
 * DTO для создания события
 */
@Data
public class NewEventDto {

    @NotBlank(message = "must not be blank")
    @Size(min = 3, max = 120, message = "Title be between 3 and 120 characters")
    private String title;

    @NotBlank(message = "must not be blank")
    @Size(min = 20, max = 2000, message = "Annotation be between 20 and 2000 characters")
    private String annotation;

    @NotNull
    private int category;

    @NotBlank(message = "must not be blank")
    @Size(min = 20, max = 7000, message = "Description be between 20 and 2000 characters")
    private String description;

    @NotNull
    @FutureOrPresent(message = "must be a date in the present or in the future")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = TIME_FORMAT)
    private LocalDateTime eventDate;

    @NotNull
    private Location location;

    @NotNull
    private boolean paid;

    @PositiveOrZero
    private int participantLimit = 0;
    private boolean requestModeration = true;
}
