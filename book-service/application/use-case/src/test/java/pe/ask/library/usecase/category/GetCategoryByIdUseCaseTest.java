package pe.ask.library.usecase.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.category.Category;
import pe.ask.library.port.out.persistence.ICategoryRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCategoryByIdUseCaseTest {

    @Mock
    private ICategoryRepository repository;

    private GetCategoryByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetCategoryByIdUseCase(repository);
    }

    @Test
    @DisplayName("Should return category when found")
    void getCategoryByIdSuccess() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        when(repository.getCategoryById(id)).thenReturn(Mono.just(category));

        StepVerifier.create(useCase.getCategoryById(id))
                .expectNext(category)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return error when category not found")
    void getCategoryByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.getCategoryById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getCategoryById(id))
                .expectError(RuntimeException.class)
                .verify();
    }
}