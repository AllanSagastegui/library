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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveBookUseCaseTest {

    @Mock
    private IBookRepository repository;

    private SaveBookUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new SaveBookUseCase(repository);
    }

    @Test
    @DisplayName("Should save book successfully")
    void saveBook() {
        Book book = new Book();
        Book savedBook = new Book();

        when(repository.saveBook(book)).thenReturn(Mono.just(savedBook));

        StepVerifier.create(useCase.saveBook(book))
                .expectNext(savedBook)
                .verifyComplete();
    }
}