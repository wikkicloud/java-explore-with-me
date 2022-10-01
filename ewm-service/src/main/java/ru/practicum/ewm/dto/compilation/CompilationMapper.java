package ru.practicum.ewm.dto.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.model.compilation.Compilation;
import ru.practicum.ewm.service.event.EventMapperServiceImp;
import ru.practicum.ewm.dto.event.EventShortDto;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapperServiceImp eventMapper;

    public Compilation toCompilation(NewCompilationDto compilationDto) {
        return Compilation.builder()
                .title(compilationDto.getTitle())
                .pinned(compilationDto.isPinned())
                .build();
    }

    public CompilationDto toCompilationDto(Compilation compilation) {
        List<EventShortDto> eventShortDtoList = compilation.getEvents().stream()
                .map(eventMapper::toEventShortDto)
                .collect(Collectors.toList());

        return new CompilationDto(compilation.getId(), compilation.getTitle(), compilation.isPinned(),
                eventShortDtoList);
    }

}
