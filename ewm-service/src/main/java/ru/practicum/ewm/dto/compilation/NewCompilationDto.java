package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * DTO для создания категории
 */
@Data
@AllArgsConstructor
public class NewCompilationDto {
    @NotNull
    private List<Integer> events;

    private boolean pinned;

    @NotBlank
    private String title;
}
