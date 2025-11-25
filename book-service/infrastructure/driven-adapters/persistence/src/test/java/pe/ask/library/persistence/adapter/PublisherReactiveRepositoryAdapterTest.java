package pe.ask.library.persistence.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import pe.ask.library.model.publisher.Publisher;
import pe.ask.library.persistence.entity.PublisherEntity;
import pe.ask.library.persistence.repository.IPublisherReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PublisherReactiveRepositoryAdapterTest {

    @Mock
    private IPublisherReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private PublisherReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PublisherReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    @DisplayName("Should get all publishers paginated")
    void getAllPublishers() {
        PublisherEntity entity = new PublisherEntity();
        Publisher domain = new Publisher();

        when(repository.findAllPaginated(0, 10)).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Publisher.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAllPublishers(0, 10))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get publisher by ID")
    void getPublisherById() {
        UUID id = UUID.randomUUID();
        PublisherEntity entity = new PublisherEntity();
        Publisher domain = new Publisher();

        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Publisher.class)).thenReturn(domain);

        StepVerifier.create(adapter.getPublisherById(id))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get publishers by name with format")
    void getPublishersByName() {
        PublisherEntity entity = new PublisherEntity();
        Publisher domain = new Publisher();

        when(repository.findPublisherByNamePaginated(0, 10, "%O'Reilly%")).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Publisher.class)).thenReturn(domain);

        StepVerifier.create(adapter.getPublishersByName(0, 10, "O'Reilly"))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get publishers by country with format")
    void getAllPublishersByCountry() {
        PublisherEntity entity = new PublisherEntity();
        Publisher domain = new Publisher();

        when(repository.findPublisherByCountryPaginated(0, 10, "%USA%")).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Publisher.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAllPublishersByCountry(0, 10, "USA"))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should count all publishers")
    void countAll() {
        when(repository.countAll()).thenReturn(Mono.just(10L));
        StepVerifier.create(adapter.countAll())
                .expectNext(10L)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should count all by name")
    void countAllByName() {
        when(repository.countAllByName("%Pub%")).thenReturn(Mono.just(3L));
        StepVerifier.create(adapter.countAllByName("Pub"))
                .expectNext(3L)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should count all by country")
    void countAllByCountry() {
        when(repository.countAllByCountry("%UK%")).thenReturn(Mono.just(2L));
        StepVerifier.create(adapter.countAllByCountry("UK"))
                .expectNext(2L)
                .verifyComplete();
    }
}