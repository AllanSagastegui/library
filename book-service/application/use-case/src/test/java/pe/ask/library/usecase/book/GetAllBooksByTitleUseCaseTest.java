package pe.ask.library.usecase.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.book.Book;
import pe.ask.library.model.book.BookComplete;
import pe.ask.library.port.out.persistence.IBookRepository;
import pe.ask.library.usecase.utils.MapBookToComplete;
import pe.ask.library.usecase.utils.PaginationUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllBooksByTitleUseCaseTest {

    @Mock
    private IBookRepository repository;
    @Mock
    private MapBookToComplete mapBookToComplete;

    private PaginationUtils utils;

    private GetAllBooksByTitleUseCase useCase;

    @BeforeEach
    void setUp() {
        utils = new PaginationUtils();
        useCase = new GetAllBooksByTitleUseCase(repository, mapBookToComplete, utils);
    }

    @Test
    @DisplayName("Should return paged books by title")
    void getAllBooksByTitle() {
        String title = "Java";
        int page = 0;
        int size = 10;

        Book book = new Book();
        BookComplete bookComplete = mock(BookComplete.class);

        when(repository.getAllBooksByTitle(page, size, title)).thenReturn(Flux.just(book));
        when(mapBookToComplete.map(book)).thenReturn(Mono.just(bookComplete));
        when(repository.countAllByTitle(title)).thenReturn(Mono.just(1L));

        StepVerifier.create(useCase.getAllBooksByTitle(page, size, title))
                .expectNextMatches(pageable ->
                        pageable.getTotalElements() == 1L &&
                                !pageable.getContent().isEmpty()
                )
                .verifyComplete();
    }
}