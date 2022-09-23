package ru.practicum.ewm.stats.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

import static ru.practicum.ewm.stats.util.Constant.TIME_FORMAT;

@Entity
@Table(name = "hits")
@Getter
@Setter
@ToString
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String app;
    private String uri;
    private String ip;
    @JsonFormat(pattern = TIME_FORMAT)
    private LocalDateTime timestamp;
}
