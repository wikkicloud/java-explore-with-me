package ru.practicum.ewm.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.practicum.ewm.util.Constant.TIME_FORMAT;

@Service
public class HttpClient {
    RestTemplate restTemplate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(TIME_FORMAT);

    @Autowired
    public HttpClient(@Value("${statistic-server.url}") String serverUrl, RestTemplateBuilder builder) {
        this.restTemplate = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        Map<String, Object> parameters;
        String path;
        if (start != null && end != null) {
            parameters = Map.of(
                    "start", start.format(formatter),
                    "end", end.format(formatter),
                    "uris", uris.toArray(),
                    "unique", unique);
            path = "/stats?start={start}&end={end}&uris={uris}&unique={unique}";

        } else {
            parameters = Map.of(
                    "uris", uris.toArray(),
                    "unique", unique);
            path = "/stats?uris={uris}&unique={unique}";
        }
        try {
            ResponseEntity<List<ViewStats>> viewStats = restTemplate.exchange(
                    path, HttpMethod.GET, null,
                    new ParameterizedTypeReference<>() {
                    }, parameters);
            if (viewStats.hasBody()) {
                return viewStats.getBody();
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void setHit(String path, EndpointHit endpointHit) {
        HttpEntity<Object> requestEntity = new HttpEntity<>(endpointHit);
        try {
            restTemplate.exchange(
                    path,
                    HttpMethod.POST,
                    requestEntity,
                    Object.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }
}

