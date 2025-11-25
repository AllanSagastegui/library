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
class GetAllCategoriesUseCaseTest {

    @Mock
    private ICategoryRepository repository;

    @Mock
    private IPaginationUtils utils;

    private GetAllCategoriesUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAllCategoriesUseCase(repository, utils);
    }

    @Test
    @DisplayName("Should return pageable of all categories")
    void getAllCategories() {
        int page = 0;
        int size = 10;

        when(repository.getAllCategories(page, size)).thenReturn(Flux.just(new Category()));
        when(repository.countAll()).thenReturn(Mono.just(5L));

        Pageable<Category> pageable = mock(Pageable.class);

        when(utils.<Category>createPageable(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(pageable));

        StepVerifier.create(useCase.getAllCategories(page, size))
                .expectNext(pageable)
                .verifyComplete();
    }
}
