package pe.ask.library.usecase.publisher;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.publisher.Publisher;
import pe.ask.library.port.out.persistence.IPublisherRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPublisherByIdUseCaseTest {

    @Mock
    private IPublisherRepository repository;

    private GetPublisherByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetPublisherByIdUseCase(repository);
    }

    @Test
    @DisplayName("Should return publisher when found")
    void getPublisherByIdSuccess() {
        UUID id = UUID.randomUUID();
        Publisher publisher = new Publisher();
        when(repository.getPublisherById(id)).thenReturn(Mono.just(publisher));

        StepVerifier.create(useCase.getPublisherById(id))
                .expectNext(publisher)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return error when publisher not found")
    void getPublisherByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.getPublisherById(id)).thenReturn(Mono.empty());

        StepVerifier.create(useCase.getPublisherById(id))
                .expectError(RuntimeException.class)
                .verify();
    }
}
