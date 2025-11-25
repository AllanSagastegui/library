package pe.ask.library.usecase.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.book.Book;
import pe.ask.library.port.out.persistence.IBookRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetBookByIdUseCaseTest {

    @Mock
    private IBookRepository repository;

    private GetBookByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetBookByIdUseCase(repository);
    }

    @Test
    @DisplayName("Should return book when found")
    void getBookByIdSuccess() {
        UUID id = UUID.randomUUID();
        Book book = new Book();
        when(repository.getBookById(id)).thenReturn(Mono.just(book));

        StepVerifier.create(useCase.getBookById(id))
                .expectNext(book)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should error when book not found")
    void getBookByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.getBookById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getBookById(id))
                .expectError(RuntimeException.class)
                .verify();
    }
}
