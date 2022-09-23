package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.model.Category;

import java.util.List;

public interface CategoryService {
    Category create(Category category);

    Category get(int id);

    Category update(int id, Category category);

    void remove(int id);

    List<Category> findAll(int from, int size);
}
