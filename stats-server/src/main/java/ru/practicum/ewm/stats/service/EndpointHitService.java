package ru.practicum.ewm.stats.service;

import ru.practicum.ewm.stats.model.EndpointHit;
import ru.practicum.ewm.stats.repository.IViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitService {
    EndpointHit add(EndpointHit hit);

    List<IViewStats> stats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
