package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    /*ADMIN Permission*/
    @PostMapping("/admin/categories")
    public CategoryDto create(@Valid @RequestBody CategoryDto categoryDto) {
        Category category = categoryService.create(CategoryMapper.toCategory(categoryDto));
        log.info("Add category={}", category);
        return CategoryMapper.toCategoryDto(category);
    }

    @GetMapping("/admin/categories/{id}")
    public CategoryDto findByIdAsAdmin(@PathVariable int id) {
        return CategoryMapper.toCategoryDto(categoryService.get(id));
    }

    @PatchMapping("/admin/categories")
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Update category={}", categoryDto);
        Category category = categoryService.update(categoryDto.getId(), CategoryMapper.toCategory(categoryDto));
        return CategoryMapper.toCategoryDto(category);
    }

    @DeleteMapping("/admin/categories/{id}")
    public void remove(@PathVariable int id) {
        log.info("Remove category id={}", id);
        categoryService.remove(id);
    }

    /*PUBLIC Permission*/
    @GetMapping("/categories")
    public List<CategoryDto> findAll(
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        return categoryService.findAll(from, size).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/categories/{id}")
    public CategoryDto findById(@PathVariable int id) {
        return CategoryMapper.toCategoryDto(categoryService.get(id));
    }

}
