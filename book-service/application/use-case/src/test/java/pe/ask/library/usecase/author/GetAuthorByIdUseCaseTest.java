package pe.ask.library.usecase.author;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.author.Author;
import pe.ask.library.port.out.persistence.IAuthorRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAuthorByIdUseCaseTest {

    @Mock
    private IAuthorRepository repository;

    private GetAuthorByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAuthorByIdUseCase(repository);
    }

    @Test
    @DisplayName("Should return author when found")
    void getAuthorByIdSuccess() {
        UUID id = UUID.randomUUID();
        Author author = new Author();
        when(repository.getAuthorById(id)).thenReturn(Mono.just(author));

        StepVerifier.create(useCase.getAuthorById(id))
                .expectNext(author)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return error when author not found")
    void getAuthorByIdError() {
        UUID id = UUID.randomUUID();
        when(repository.getAuthorById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getAuthorById(id))
                .expectError(RuntimeException.class)
                .verify();
    }
}