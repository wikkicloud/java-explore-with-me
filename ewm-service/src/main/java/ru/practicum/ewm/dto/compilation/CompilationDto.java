package ru.practicum.ewm.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.ewm.dto.event.EventShortDto;

import java.util.List;

@AllArgsConstructor
@Data
public class CompilationDto {
    private int id;
    private String title;
    private boolean pinned;
    private List<EventShortDto> events;
}
