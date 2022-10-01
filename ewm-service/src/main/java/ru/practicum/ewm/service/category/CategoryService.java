package ru.practicum.ewm.service.category;

import ru.practicum.ewm.model.category.Category;

import java.util.List;

public interface CategoryService {
    /**
     * Добавление категории
     * @param category Category
     * @return Category
     */
    Category create(Category category);

    /**
     * Получение категории по id
     * @param id категории
     * @return Category
     */
    Category get(int id);

    /**
     * Обновление категории
     * @param id категории
     * @param category Category
     * @return Category
     */
    Category update(int id, Category category);

    /**
     * Удаление категории
     * @param id категории
     */
    void remove(int id);

    /**
     * Полчучение всех категорий
     * @param from инедекс начала
     * @param size размер страницы
     * @return List<Category>
     */
    List<Category> findAll(int from, int size);
}
