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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveAuthorUseCaseTest {

    @Mock
    private IAuthorRepository repository;

    private SaveAuthorUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SaveAuthorUseCase(repository);
    }

    @Test
    @DisplayName("Should save author when pseudonym is unique")
    void saveAuthorSuccess() {
        Author author = Author.builder().withPseudonym("unique").build();
        Author savedAuthor = Author.builder().withId(UUID.randomUUID()).withPseudonym("unique").build();

        when(repository.getAuthorByPseudonym(author.getPseudonym())).thenReturn(Mono.empty());
        when(repository.saveAuthor(author)).thenReturn(Mono.just(savedAuthor));

        StepVerifier.create(useCase.saveAuthor(author))
                .expectNext(savedAuthor)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return error when pseudonym already exists")
    void saveAuthorErrorDuplicate() {
        String pseudonym = "duplicate";
        Author author = Author.builder().withPseudonym(pseudonym).build();

        Author existingAuthor = Author.builder()
                .withId(UUID.randomUUID())
                .withPseudonym(pseudonym)
                .build();

        doReturn(Mono.just(existingAuthor)).when(repository).getAuthorByPseudonym(pseudonym);

        lenient().when(repository.saveAuthor(any(Author.class))).thenReturn(Mono.empty());

        StepVerifier.create(useCase.saveAuthor(author))
                .expectError(RuntimeException.class)
                .verify();
    }
}