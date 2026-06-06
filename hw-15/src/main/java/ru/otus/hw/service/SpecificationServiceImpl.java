package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.model.Specification;
import ru.otus.hw.model.TestCase;
import ru.otus.hw.model.Type;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpecificationServiceImpl implements SpecificationService {

    private final TestGateway testGateway;

    @Override
    public void generateSpecifications() {
        List<Specification> spec = createSpecifications();
        ForkJoinPool pool = ForkJoinPool.commonPool();
        for (Specification specification : spec) {
            pool.execute(() -> {
                log.info("New Specifications #{}",
                    specification.getId());
                Collection<TestCase> testCases = testGateway.process(specification);
                log.info("Created {} test cases for specification #{}: {}, withAutotest: {}",
                    testCases.size(), specification.getId(),
                    testCases.stream()
                        .map(TestCase::getName)
                        .collect(Collectors.joining(",")),
                    testCases.stream()
                        .filter(tc -> !tc.getAutoTests().isEmpty())
                        .count());
            });
        }
    }

    private List<Specification> createSpecifications() {
        return List.of(Specification.builder()
                .id(1L)
                .text("Описание процесса А")
                .author("author 1")
                .type(Type.PROCESS)
                .build(),
            Specification.builder()
                .id(2L)
                .text("Описание операции А")
                .author("author 2")
                .type(Type.OPERATION)
                .build(),
            Specification.builder()
                .id(3L)
                .text("Описание валидации А")
                .author("author 3")
                .type(Type.VALIDATION)
                .build());
    }
}
