package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ApiError> handleNoSuchElementException(NoSuchElementException e) {
        log.error(e.getMessage());
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(
                new ApiError(e.getMessage(), "The required object was not found.", httpStatus), httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage());
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        return new ResponseEntity<>(
                new ApiError(e.getMessage(), "Integrity constraint has been violated", httpStatus), httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleNotConditionException(NotConditionException e) {
        log.error(e.getMessage());
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(
                new ApiError(e.getMessage(), "For the requested operation the conditions are not met.",
                        httpStatus), httpStatus);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        List<String> errors = new ArrayList<>();
        BindingResult bindingResult = ex.getBindingResult();
        String message = String.format("During [%s] validation %s errors were found", bindingResult.getObjectName(),
                bindingResult.getErrorCount());
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.add("Field: " + error.getField() + ". Error: " + error.getDefaultMessage() + ". Value: " +
                    error.getRejectedValue());
        }
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
                new ApiError(errors, message, "Incorrectly made request.", httpStatus), httpStatus);
    }
}
