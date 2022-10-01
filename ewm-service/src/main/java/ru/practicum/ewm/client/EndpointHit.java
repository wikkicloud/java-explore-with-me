package ru.practicum.ewm.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

import static ru.practicum.ewm.util.Constant.TIME_FORMAT;

@Builder
@Getter
@Setter
@ToString
public class EndpointHit {
    private String app;
    private String uri;
    private String ip;
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime timestamp;
}
