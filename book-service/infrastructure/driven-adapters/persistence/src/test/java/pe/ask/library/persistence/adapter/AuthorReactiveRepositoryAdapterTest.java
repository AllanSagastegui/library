package pe.ask.library.persistence.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import pe.ask.library.model.author.Author;
import pe.ask.library.persistence.entity.AuthorEntity;
import pe.ask.library.persistence.repository.IAuthorReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorReactiveRepositoryAdapterTest {

    @Mock
    private IAuthorReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private AuthorReactiveRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new AuthorReactiveRepositoryAdapter(repository, mapper);
    }

    @Test
    @DisplayName("Should get all authors paginated")
    void getAllAuthors() {
        AuthorEntity entity = new AuthorEntity();
        Author domain = new Author();

        when(repository.findAllPaginated(0, 10)).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Author.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAllAuthors(0, 10))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get all authors by nationality with formatted query")
    void getAllAuthorsByNationality() {
        String nationality = "Peru";
        AuthorEntity entity = new AuthorEntity();
        Author domain = new Author();

        when(repository.findAllAuthorsByNationality(0, 10, "%Peru%")).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Author.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAllAuthorsByNationality(0, 10, nationality))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get author by ID")
    void getAuthorById() {
        UUID id = UUID.randomUUID();
        AuthorEntity entity = new AuthorEntity();
        Author domain = new Author();

        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Author.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAuthorById(id))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get author by pseudonym")
    void getAuthorByPseudonym() {
        String pseudonym = "Writer";
        AuthorEntity entity = new AuthorEntity();
        Author domain = new Author();

        when(repository.findAuthorByPseudonymIsContainingIgnoreCase(pseudonym)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Author.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAuthorByPseudonym(pseudonym))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should save author")
    void saveAuthor() {
        Author domain = new Author();
        AuthorEntity entity = new AuthorEntity();

        when(mapper.map(domain, AuthorEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Author.class)).thenReturn(domain);

        StepVerifier.create(adapter.saveAuthor(domain))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should update author ensuring ID is set")
    void updateAuthor() {
        UUID id = UUID.randomUUID();
        Author domain = new Author();
        AuthorEntity entity = new AuthorEntity();

        when(mapper.map(domain, AuthorEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Author.class)).thenReturn(domain);

        StepVerifier.create(adapter.updateAuthor(id, domain))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should count all authors")
    void countAll() {
        when(repository.countAll()).thenReturn(Mono.just(10L));

        StepVerifier.create(adapter.countAll())
                .expectNext(10L)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should count all authors by nationality with formatted query")
    void countAllByNationality() {
        String nationality = "Peru";
        when(repository.countAllByNationality("%Peru%")).thenReturn(Mono.just(5L));

        StepVerifier.create(adapter.countAllByNationality(nationality))
                .expectNext(5L)
                .verifyComplete();
    }
}