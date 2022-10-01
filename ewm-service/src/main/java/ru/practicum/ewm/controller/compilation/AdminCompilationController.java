package ru.practicum.ewm.controller.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.CompilationMapper;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.model.compilation.Compilation;
import ru.practicum.ewm.service.compilation.CompilationService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationController {
    private final CompilationService compilationService;
    private final CompilationMapper compilationMapper;

    @PostMapping
    public CompilationDto add(@Valid @RequestBody NewCompilationDto compilationDto) {
        log.info("Add compilation={}", compilationDto);
        Compilation compilation = compilationService.add(compilationDto.getEvents(),
                compilationMapper.toCompilation(compilationDto));
        return compilationMapper.toCompilationDto(compilation);
    }

    @DeleteMapping("/{compId}")
    public void remove(@PathVariable int compId) {
        log.info("Remove compilationId={}", compId);
        compilationService.remove(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void removeEventFromCompilation(@PathVariable int compId, @PathVariable int eventId) {
        log.info("Remove eventId={} from compId={}", eventId, compId);
        compilationService.removeEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@PathVariable int compId, @PathVariable int eventId) {
        log.info("Add eventId={} to compId={}", eventId, compId);
        compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void disablePinFromCompilation(@PathVariable int compId) {
        log.info("Disable pin from compId={}", compId);
        compilationService.disablePinFromCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void enablePinFromCompilation(@PathVariable int compId) {
        log.info("Enable pin from compId={}", compId);
        compilationService.enablePinFromCompilation(compId);
    }
}
