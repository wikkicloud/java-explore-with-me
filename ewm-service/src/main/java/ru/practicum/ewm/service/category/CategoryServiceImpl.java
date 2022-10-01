package ru.practicum.ewm.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.repository.category.CategoryRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category get(int id) {
        return categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                        String.format("category id=%s not found", id)
                )
        );
    }

    @Override
    public Category update(int id, Category category) {
        Category categoryUpdated = categoryRepository.findById(id).orElseThrow(() -> new NoSuchElementException(
                        String.format("category id=%s not found", id)
                )
        );
        categoryUpdated.setName(category.getName());
        return categoryRepository.save(category);
    }

    @Override
    public void remove(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> findAll(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from / size, size)).getContent();
    }
}
