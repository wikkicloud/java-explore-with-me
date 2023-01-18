package ru.practicum.ewm.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.CompilationMapper;
import ru.practicum.ewm.service.compilation.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationController {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @GetMapping
    public List<CompilationDto> findCompilation(
            @RequestParam(name = "pinned", required = false, defaultValue = "true") boolean pinned,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
            @Positive @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        log.info("Get compilations pinned={}, from={}, size={}", pinned, from, size);
        return compilationService.findCompilation(pinned, from, size).stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{compId}")
    public CompilationDto findCompilationById(@PathVariable int compId) {
        return compilationMapper.toCompilationDto(compilationService.findById(compId));
    }
}
