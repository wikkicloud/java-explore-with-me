package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {
    Compilation add(List<Integer> ids, Compilation compilation);

    void remove(int id);

    void removeEventFromCompilation(int compId, int eventId);

    void addEventToCompilation(int compId, int eventId);

    void disablePinFromCompilation(int compId);

    void enablePinFromCompilation(int compId);

    List<Compilation> findCompilation(boolean pinned, int from, int size);

    Compilation findById(int compId);
}
