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
class GetAllAuthorsByNationalityUseCaseTest {

    @Mock
    private IAuthorRepository repository;

    @Mock
    private IPaginationUtils utils;

    private GetAllAuthorsByNationalityUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAllAuthorsByNationalityUseCase(repository, utils);
    }

    @Test
    @DisplayName("Should return pageable of authors by nationality")
    void getAllAuthorsByNationality() {
        int page = 0;
        int size = 10;
        String nationality = "PE";

        when(repository.getAllAuthorsByNationality(page, size, nationality)).thenReturn(Flux.just(new Author()));
        when(repository.countAllByNationality(nationality)).thenReturn(Mono.just(1L));

        Pageable<Author> pageable = mock(Pageable.class);

        when(utils.<Author>createPageable(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(pageable));

        StepVerifier.create(useCase.getAllAuthorsByNationality(page, size, nationality))
                .expectNext(pageable)
                .verifyComplete();
    }
}