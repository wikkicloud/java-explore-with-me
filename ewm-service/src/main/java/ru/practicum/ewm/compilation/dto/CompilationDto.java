package ru.practicum.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.event.dto.EventShortDto;

import java.util.List;

@AllArgsConstructor
@Data
public class CompilationDto {
    private int id;
    private String title;
    private boolean pinned;
    private List<EventShortDto> events;
}
