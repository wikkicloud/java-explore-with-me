package ru.practicum.ewm.compilation.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.service.EventMapperServiceImp;
import ru.practicum.ewm.event.dto.EventShortDto;

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
