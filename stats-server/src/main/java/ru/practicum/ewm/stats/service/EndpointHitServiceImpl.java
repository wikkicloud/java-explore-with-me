package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.model.EndpointHit;
import ru.practicum.ewm.stats.repository.EndpointHitRepository;
import ru.practicum.ewm.stats.repository.IViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;

    @Override
    public EndpointHit add(EndpointHit hit) {
        return endpointHitRepository.save(hit);
    }

    @Override
    public List<IViewStats> stats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (start == null && end == null)
            return endpointHitRepository.findAllStats(uris);
        if (unique) {
            return endpointHitRepository.findStatsInUrisOnlyUniqueIp(start, end, uris);
        } else {
            return endpointHitRepository.findStatsInUris(start, end, uris);
        }
    }
}
