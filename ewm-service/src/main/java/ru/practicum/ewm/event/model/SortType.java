package ru.practicum.ewm.event.model;

import java.util.Optional;

public enum SortType {
    EVENT_DATE,
    VIEWS;

    public static Optional<SortType> from(String sortType) {
        for (SortType state : values()) {
            if (state.name().equalsIgnoreCase(sortType)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
