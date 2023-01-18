package ru.practicum.ewm.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.stats.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHit, Integer> {


    //Get all hits with a unique IP and in uris
    @Query("SELECT e.app AS app, e.uri AS uri, COUNT(DISTINCT e.ip) AS hits " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp > ?1 AND e.timestamp < ?2 AND e.uri IN ?3 " +
            "GROUP BY e.uri, e.app")
    List<IViewStats> findStatsInUrisOnlyUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);

    //Get all hits in uris
    @Query("SELECT e.app AS app, e.uri AS uri, COUNT(e.ip) AS hits " +
            "FROM EndpointHit AS e " +
            "WHERE e.timestamp > ?1 AND e.timestamp < ?2 AND e.uri IN ?3 " +
            "GROUP BY e.uri, e.app")
    List<IViewStats> findStatsInUris(LocalDateTime start, LocalDateTime end, List<String> uris);

    //Get all hits from start to end
    @Query("SELECT e.app AS app, e.uri AS uri, COUNT(e.ip) AS hits " +
            "FROM EndpointHit AS e " +
            "WHERE e.uri IN ?1 " +
            "GROUP BY e.uri, e.app")
    List<IViewStats> findAllStats(List<String> uris);

}
