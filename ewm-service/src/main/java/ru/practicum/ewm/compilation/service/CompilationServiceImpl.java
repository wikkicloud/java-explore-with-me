package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotConditionException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public Compilation add(List<Integer> ids, Compilation compilation) {
        Set<Event> eventSet = eventRepository.findByIdIn(ids);
        compilation.setEvents(eventSet);
        return compilationRepository.save(compilation);
    }

    @Override
    public void remove(int id) {
        compilationRepository.deleteById(id);
    }

    @Override
    public void removeEventFromCompilation(int compId, int eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NoSuchElementException(
                String.format("compilation with id=%s was not found.", compId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(
                String.format("event with id=%s was not found.", eventId)));
        Set<Event> eventsUpdated = compilation.getEvents();
        if (!eventsUpdated.contains(event))
            throw new NotConditionException(String.format("event id=%s not found in compilation id=%s", eventId,
                    compId));
        eventsUpdated.remove(event);
        compilation.setEvents(eventsUpdated);
        compilationRepository.save(compilation);
    }

    @Override
    public void addEventToCompilation(int compId, int eventId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NoSuchElementException(
                String.format("compilation with id=%s was not found.", compId)));
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NoSuchElementException(
                String.format("event with id=%s was not found.", eventId)));
        Set<Event> eventsUpdated = compilation.getEvents();
        if (eventsUpdated.contains(event))
            throw new NotConditionException(String.format("event id=%s contains in compilation id=%s", eventId,
                    compId));
        eventsUpdated.add(event);
        compilation.setEvents(eventsUpdated);
        compilationRepository.save(compilation);
    }

    @Override
    public void disablePinFromCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NoSuchElementException(
                String.format("compilation with id=%s was not found.", compId)));
        if (!compilation.isPinned())
            throw new NotConditionException("compilation with id=%s pinned is false");
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void enablePinFromCompilation(int compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NoSuchElementException(
                String.format("compilation with id=%s was not found.", compId)));
        if (compilation.isPinned())
            throw new NotConditionException("compilation with id=%s pinned is true");
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public List<Compilation> findCompilation(boolean pinned, int from, int size) {
        return compilationRepository.findByPinnedIs(pinned, PageRequest.of(from / size, size));
    }

    @Override
    public Compilation findById(int compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> new NoSuchElementException(
                String.format("compilation with id=%s was not found.", compId)));
    }
}
