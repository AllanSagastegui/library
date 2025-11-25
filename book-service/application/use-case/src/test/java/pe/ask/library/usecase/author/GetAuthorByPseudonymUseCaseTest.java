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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAuthorByPseudonymUseCaseTest {

    @Mock
    private IAuthorRepository repository;

    private GetAuthorByPseudonymUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAuthorByPseudonymUseCase(repository);
    }

    @Test
    @DisplayName("Should return author when pseudonym found")
    void getAuthorByPseudonymSuccess() {
        String pseudonym = "writer";
        Author author = new Author();
        when(repository.getAuthorByPseudonym(pseudonym)).thenReturn(Mono.just(author));

        StepVerifier.create(useCase.getAuthorByPseudonym(pseudonym))
                .expectNext(author)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return error when pseudonym not found")
    void getAuthorByPseudonymError() {
        String pseudonym = "unknown";
        when(repository.getAuthorByPseudonym(pseudonym)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getAuthorByPseudonym(pseudonym))
                .expectError(RuntimeException.class)
                .verify();
    }
}