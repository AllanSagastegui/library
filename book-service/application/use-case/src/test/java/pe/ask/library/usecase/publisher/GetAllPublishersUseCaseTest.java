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
class GetAllPublishersUseCaseTest {

    @Mock
    private IPublisherRepository repository;

    @Mock
    private IPaginationUtils utils;

    private GetAllPublishersUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetAllPublishersUseCase(repository, utils);
    }

    @Test
    @DisplayName("Should return pageable of all publishers")
    void getAllPublishers() {
        int page = 0;
        int size = 10;

        when(repository.getAllPublishers(page, size)).thenReturn(Flux.just(new Publisher()));
        when(repository.countAll()).thenReturn(Mono.just(10L));

        Pageable<Publisher> pageable = mock(Pageable.class);

        when(utils.<Publisher>createPageable(any(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(pageable));

        StepVerifier.create(useCase.getAllPublishers(page, size))
                .expectNext(pageable)
                .verifyComplete();
    }
}
