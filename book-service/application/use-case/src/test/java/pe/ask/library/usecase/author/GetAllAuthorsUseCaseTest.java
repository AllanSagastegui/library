package pe.ask.library.usecase.author;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.author.Author;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.persistence.IAuthorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetAllAuthorsUseCaseTest {

    @Mock
    private IAuthorRepository repository;

    @Mock
    private IPaginationUtils utils;

    private GetAllAuthorsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAllAuthorsUseCase(repository, utils);
    }

    @Test
    @DisplayName("Should return pageable of all authors")
    void getAllAuthors() {
        int page = 0;
        int size = 10;

        when(repository.getAllAuthors(page, size)).thenReturn(Flux.just(new Author()));
        when(repository.countAll()).thenReturn(Mono.just(10L));

        Pageable<Author> pageable = mock(Pageable.class);

        when(utils.<Author>createPageable(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(pageable));

        StepVerifier.create(useCase.getAllAuthors(page, size))
                .expectNext(pageable)
                .verifyComplete();
    }
}