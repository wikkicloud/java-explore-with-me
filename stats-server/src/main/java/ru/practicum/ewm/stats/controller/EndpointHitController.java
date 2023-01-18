package ru.practicum.ewm.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.stats.model.EndpointHit;
import ru.practicum.ewm.stats.repository.IViewStats;
import ru.practicum.ewm.stats.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.ewm.stats.util.Constant.TIME_FORMAT;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EndpointHitController {
    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    public EndpointHit add(@RequestBody EndpointHit hit) {
        log.info("Add hit EndpointHit={}", hit);
        return endpointHitService.add(hit);
    }

    @GetMapping("/stats")
    public List<IViewStats> stats(
            @DateTimeFormat(pattern = TIME_FORMAT) @RequestParam(required = false) LocalDateTime start,
            @DateTimeFormat(pattern = TIME_FORMAT) @RequestParam(required = false) LocalDateTime end,
            @RequestParam(defaultValue = "") List<String> uris,
            @RequestParam(defaultValue = "false", required = false) boolean unique
    ) {
        log.info("Get stats start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return endpointHitService.stats(start, end, uris, unique);
    }
}
