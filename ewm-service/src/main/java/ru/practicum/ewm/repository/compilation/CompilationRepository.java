package ru.practicum.ewm.repository.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.compilation.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Integer> {
    /**
     * Все подборки по флагу pinned
     * @param pinned показывать на главной странице true-да, false-нет
     * @param pageable
     * @return List<Compilation>
     */
    List<Compilation> findByPinnedIs(boolean pinned, Pageable pageable);
}
