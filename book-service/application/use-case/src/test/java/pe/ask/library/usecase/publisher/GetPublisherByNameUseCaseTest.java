package pe.ask.library.usecase.publisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.publisher.Publisher;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.persistence.IPublisherRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPublisherByNameUseCaseTest {

    @Mock
    private IPublisherRepository repository;

    @Mock
    private IPaginationUtils utils;

    private GetPublisherByNameUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetPublisherByNameUseCase(repository, utils);
    }

    @Test
    @DisplayName("Should return pageable of publishers by name")
    void getPublishersByName() {
        int page = 0;
        int size = 10;
        String name = "O'Reilly";

        when(repository.getPublishersByName(page, size, name)).thenReturn(Flux.just(new Publisher()));
        when(repository.countAllByName(name)).thenReturn(Mono.just(2L));

        Pageable<Publisher> pageable = mock(Pageable.class);

        when(utils.<Publisher>createPageable(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(pageable));

        StepVerifier.create(useCase.getPublishersByName(page, size, name))
                .expectNext(pageable)
                .verifyComplete();
    }
}
