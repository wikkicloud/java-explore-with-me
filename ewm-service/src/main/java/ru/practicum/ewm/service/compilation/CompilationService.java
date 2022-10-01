package ru.practicum.ewm.service.compilation;

import ru.practicum.ewm.model.compilation.Compilation;

import java.util.List;

public interface CompilationService {
    /**
     * Добавление подборки
     * @param ids список id событий
     * @param compilation подборка
     * @return Compilation
     */
    Compilation add(List<Integer> ids, Compilation compilation);

    /**
     * Удаление подборки
     * @param id подборки
     */
    void remove(int id);

    /**
     * Удаление события из подборки
     * @param compId id подборки
     * @param eventId id события
     */
    void removeEventFromCompilation(int compId, int eventId);

    /**
     * Добавление события в подборку
     * @param compId id подборки
     * @param eventId id события
     */
    void addEventToCompilation(int compId, int eventId);

    /**
     * Отмена флага показа подборки на главной странице
     * @param compId id подборки
     */
    void disablePinFromCompilation(int compId);

    /**
     * Включение флага показа подборки на главной странице
     * @param compId id подборки
     */
    void enablePinFromCompilation(int compId);

    /**
     * Получение подборок с фильтрацией по флагу pinned
     * @param pinned true - показывать на главной странице, false - не показывать
     * @param from инедекс первого елемента
     * @param size размер страницы
     * @return List<Compilation>
     */
    List<Compilation> findCompilation(boolean pinned, int from, int size);

    /**
     * Плучение подборки по id
     * @param compId id подборки
     * @return Compilation
     */
    Compilation findById(int compId);
}
