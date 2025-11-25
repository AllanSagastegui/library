package pe.ask.library.usecase.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PaginationUtilsTest {

    private final PaginationUtils paginationUtils = new PaginationUtils();

    @Test
    @DisplayName("Should create Pageable object correctly")
    void createPageable() {
        Flux<String> content = Flux.just("A", "B", "C");
        Mono<Long> count = Mono.just(10L);
        int page = 0;
        int size = 5;

        StepVerifier.create(paginationUtils.createPageable(content, count, page, size))
                .assertNext(pageable -> {
                    assertNotNull(pageable);
                    assertEquals(page, pageable.getPage());
                    assertEquals(size, pageable.getSize());
                    assertEquals(10L, pageable.getTotalElements());
                    assertEquals(2, pageable.getTotalPages());
                    assertEquals(3, pageable.getContent().size());
                    assertEquals("A", pageable.getContent().get(0));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should calculate total pages correctly for odd numbers")
    void createPageableOdd() {
        Flux<String> content = Flux.just("A");
        Mono<Long> count = Mono.just(11L);
        int size = 5;

        StepVerifier.create(paginationUtils.createPageable(content, count, 0, size))
                .assertNext(pageable -> {
                    assertEquals(3, pageable.getTotalPages());
                })
                .verifyComplete();
    }
}
