package pe.ask.library.usecase.kafka;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBookStockUseCaseTest {

    @Mock
    private IBookRepository repository;

    private UpdateBookStockUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateBookStockUseCase(repository);
    }

    @Test
    @DisplayName("Should increment stock successfully")
    void updateBookStockIncrement() {
        UUID bookId = UUID.randomUUID();
        int quantity = 5;
        boolean isIncrement = true;

        Book existingBook = Book.builder().withId(bookId).withStock(10).build();
        Book updatedBook = Book.builder().withId(bookId).withStock(15).build();

        when(repository.getBookById(bookId)).thenReturn(Mono.just(existingBook));
        when(repository.updateBook(eq(bookId), any(Book.class))).thenReturn(Mono.just(updatedBook));

        StepVerifier.create(useCase.updateBookStock(bookId, quantity, !isIncrement))
                .verifyComplete();

        verify(repository).updateBook(eq(bookId), argThat(b -> b.getStock() == 15));
    }

    @Test
    @DisplayName("Should decrement stock successfully when sufficient")
    void updateBookStockDecrement() {
        UUID bookId = UUID.randomUUID();
        int quantity = 5;
        boolean isIncrement = false;

        Book existingBook = Book.builder().withId(bookId).withStock(10).build();
        Book updatedBook = Book.builder().withId(bookId).withStock(5).build();

        when(repository.getBookById(bookId)).thenReturn(Mono.just(existingBook));
        when(repository.updateBook(eq(bookId), any(Book.class))).thenReturn(Mono.just(updatedBook));

        StepVerifier.create(useCase.updateBookStock(bookId, quantity, !isIncrement))
                .verifyComplete();

        verify(repository).updateBook(eq(bookId), argThat(b -> b.getStock() == 5));
    }

    @Test
    @DisplayName("Should error when quantity is negative")
    void updateBookStockInvalidQuantity() {
        StepVerifier.create(useCase.updateBookStock(UUID.randomUUID(), -1, true))
                .expectError(RuntimeException.class)
                .verify();

        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Should error when book not found")
    void updateBookStockBookNotFound() {
        UUID bookId = UUID.randomUUID();
        when(repository.getBookById(bookId)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.updateBookStock(bookId, 1, true))
                .expectError(RuntimeException.class)
                .verify();
    }
}
