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
class UpdateBookUseCaseTest {

    @Mock
    private IBookRepository repository;

    private UpdateBookUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateBookUseCase(repository);
    }

    @Test
    @DisplayName("Should update book when exists")
    void updateBookSuccess() {
        UUID id = UUID.randomUUID();
        Book book = new Book();
        Book existing = new Book();
        Book updated = new Book();

        when(repository.getBookById(id)).thenReturn(Mono.just(existing));
        when(repository.updateBook(id, book)).thenReturn(Mono.just(updated));

        StepVerifier.create(useCase.updateBook(id, book))
                .expectNext(updated)
                .verifyComplete();
    }
}
