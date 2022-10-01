package ru.practicum.ewm.controller.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.category.CategoryDto;
import ru.practicum.ewm.dto.category.CategoryMapper;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.service.category.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
@Slf4j
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        Category category = categoryService.create(CategoryMapper.toCategory(categoryDto));
        log.info("Add category={}", category);
        return CategoryMapper.toCategoryDto(category);
    }

    @GetMapping("/{id}")
    public CategoryDto findByIdAsAdmin(@PathVariable int id) {
        return CategoryMapper.toCategoryDto(categoryService.get(id));
    }

    @PatchMapping
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Update category={}", categoryDto);
        Category category = categoryService.update(categoryDto.getId(), CategoryMapper.toCategory(categoryDto));
        return CategoryMapper.toCategoryDto(category);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable int id) {
        log.info("Remove category id={}", id);
        categoryService.remove(id);
    }
}
