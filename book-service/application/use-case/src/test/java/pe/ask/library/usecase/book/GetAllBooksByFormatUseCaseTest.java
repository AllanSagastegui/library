package pe.ask.library.usecase.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.book.Book;
import pe.ask.library.model.book.BookComplete;
import pe.ask.library.model.book.Format;
import pe.ask.library.port.out.persistence.IBookRepository;
import pe.ask.library.usecase.utils.MapBookToComplete;
import pe.ask.library.usecase.utils.PaginationUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllBooksByFormatUseCaseTest {

    @Mock
    private IBookRepository repository;
    @Mock
    private MapBookToComplete mapBookToComplete;

    private PaginationUtils utils;

    private GetAllBooksByFormatUseCase useCase;

    @BeforeEach
    void setUp() {
        utils = new PaginationUtils();
        useCase = new GetAllBooksByFormatUseCase(repository, mapBookToComplete, utils);
    }

    @Test
    @DisplayName("Should return paged books by format")
    void getAllBooksByFormat() {
        Format format = Format.DIGITAL;
        int page = 0;
        int size = 10;

        Book book = new Book();
        BookComplete bookComplete = mock(BookComplete.class);

        when(repository.getAllBooksByFormat(page, size, format)).thenReturn(Flux.just(book));
        when(mapBookToComplete.map(book)).thenReturn(Mono.just(bookComplete));
        when(repository.countAllByFormat(format)).thenReturn(Mono.just(1L));

        StepVerifier.create(useCase.getAllBooksByFormat(page, size, format))
                .expectNextMatches(pageable ->
                        pageable.getTotalElements() == 1L &&
                                pageable.getTotalPages() == 1 &&
                                !pageable.getContent().isEmpty()
                )
                .verifyComplete();
    }
}