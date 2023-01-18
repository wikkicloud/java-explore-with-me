package ru.practicum.ewm.client;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Builder
@Getter
@Setter
@ToString
public class ViewStats implements Serializable {
    private String app;
    private String uri;
    private int hits;
}
