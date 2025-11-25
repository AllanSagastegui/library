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
class UpdateAuthorUseCaseTest {

    @Mock
    private IAuthorRepository repository;

    private UpdateAuthorUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateAuthorUseCase(repository);
    }

    @Test
    @DisplayName("Should update author when found and pseudonym is unique")
    void updateAuthorSuccess() {
        UUID id = UUID.randomUUID();
        Author author = Author.builder().withPseudonym("newNick").build();
        Author existing = Author.builder().withId(id).build();

        when(repository.getAuthorById(id)).thenReturn(Mono.just(existing));
        when(repository.getAuthorByPseudonym("newNick")).thenReturn(Mono.empty());
        when(repository.updateAuthor(id, author)).thenReturn(Mono.just(author));

        StepVerifier.create(useCase.updateAuthor(id, author))
                .expectNext(author)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should update author when pseudonym belongs to same author")
    void updateAuthorSuccessSamePseudonym() {
        UUID id = UUID.randomUUID();
        String pseudonym = "sameNick";
        Author author = Author.builder().withPseudonym(pseudonym).build();
        Author existing = Author.builder().withId(id).withPseudonym(pseudonym).build();

        when(repository.getAuthorById(id)).thenReturn(Mono.just(existing));
        when(repository.getAuthorByPseudonym(pseudonym)).thenReturn(Mono.just(existing));
        when(repository.updateAuthor(id, author)).thenReturn(Mono.just(author));

        StepVerifier.create(useCase.updateAuthor(id, author))
                .expectNext(author)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should fail when author ID not found")
    void updateAuthorNotFound() {
        UUID id = UUID.randomUUID();
        Author author = new Author();

        when(repository.getAuthorById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateAuthor(id, author))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Should fail when pseudonym is taken by another author")
    void updateAuthorDuplicatePseudonym() {
        UUID id = UUID.randomUUID();
        UUID otherId = UUID.randomUUID();
        String pseudonym = "taken";

        Author author = Author.builder().withPseudonym(pseudonym).build();
        Author existing = Author.builder().withId(id).build();
        Author otherAuthor = Author.builder().withId(otherId).withPseudonym(pseudonym).build();

        when(repository.getAuthorById(id)).thenReturn(Mono.just(existing));
        when(repository.getAuthorByPseudonym(pseudonym)).thenReturn(Mono.just(otherAuthor));

        StepVerifier.create(useCase.updateAuthor(id, author))
                .expectError(RuntimeException.class)
                .verify();
    }
}