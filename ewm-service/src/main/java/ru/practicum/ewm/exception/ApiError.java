package ru.practicum.ewm.exception;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.practicum.ewm.util.Constant.TIME_FORMAT;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class ApiError {
    private List<String> errors;
    private final String message;
    private final String reason;
    private final HttpStatus status;
    private final String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_FORMAT));
}
