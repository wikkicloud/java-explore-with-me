package ru.practicum.ewm.compilation.controller;

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
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.CompilationMapper;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CompilationController {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    /*Admin permission*/
    @PostMapping("/admin/compilations")
    public CompilationDto add(@Valid @RequestBody NewCompilationDto compilationDto) {
        log.info("Add compilation={}", compilationDto);
        Compilation compilation = compilationService.add(compilationDto.getEvents(),
                compilationMapper.toCompilation(compilationDto));
        return compilationMapper.toCompilationDto(compilation);
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void remove(@PathVariable int compId) {
        log.info("Remove compilationId={}", compId);
        compilationService.remove(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void removeEventFromCompilation(@PathVariable int compId, @PathVariable int eventId) {
        log.info("Remove eventId={} from compId={}", eventId, compId);
        compilationService.removeEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable int compId, @PathVariable int eventId) {
        log.info("Add eventId={} to compId={}", eventId, compId);
        compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public void disablePinFromCompilation(@PathVariable int compId) {
        log.info("Disable pin from compId={}", compId);
        compilationService.disablePinFromCompilation(compId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public void enablePinFromCompilation(@PathVariable int compId) {
        log.info("Enable pin from compId={}", compId);
        compilationService.enablePinFromCompilation(compId);
    }

    /*Public permission*/
    @GetMapping("/compilations")
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

    @GetMapping("/compilations/{compId}")
    public CompilationDto findCompilationById(@PathVariable int compId) {
        return compilationMapper.toCompilationDto(compilationService.findById(compId));
    }
}
