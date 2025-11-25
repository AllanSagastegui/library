package pe.ask.library.usecase.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.category.Category;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.persistence.ICategoryRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCategoryByNameUseCaseTest {

    @Mock
    private ICategoryRepository repository;

    @Mock
    private IPaginationUtils utils;

    private GetCategoryByNameUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetCategoryByNameUseCase(repository, utils);
    }

    @Test
    @DisplayName("Should return pageable of categories by name")
    void getCategoryByName() {
        int page = 0;
        int size = 10;
        String name = "Fiction";

        when(repository.getCategoriesByName(page, size, name)).thenReturn(Flux.just(new Category()));
        when(repository.countAllByName(name)).thenReturn(Mono.just(2L));

        Pageable<Category> pageable = mock(Pageable.class);

        when(utils.<Category>createPageable(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(pageable));

        StepVerifier.create(useCase.getCategoryByName(page, size, name))
                .expectNext(pageable)
                .verifyComplete();
    }
}