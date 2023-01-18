package ru.practicum.ewm.model.event;

import java.util.Optional;

/**
 * Типы сортировки
 */
public enum SortType {
    EVENT_DATE,
    VIEWS;

    /**
     * Валидация строки сортировки
     * @param sortType
     * @return Optional<SortType>
     */
    public static Optional<SortType> from(String sortType) {
        for (SortType state : values()) {
            if (state.name().equalsIgnoreCase(sortType)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
