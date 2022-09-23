package ru.practicum.ewm.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class CategoryDto {
    private int id;

    @NotBlank
    @NotNull
    private String name;
}
