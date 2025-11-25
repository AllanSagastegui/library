package pe.ask.library.persistence.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import pe.ask.library.model.book.Book;
import pe.ask.library.model.book.Format;
import pe.ask.library.persistence.entity.BookEntity;
import pe.ask.library.persistence.repository.IBookReactiveRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookReactiveRepositoryAdapterTest {

    @Mock
    private IBookReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private BookReactiveRepositoryAdapter adapter;

    private BookEntity entity;
    private Book domain;

    @BeforeEach
    void setUp() {
        adapter = new BookReactiveRepositoryAdapter(repository, mapper);
        entity = new BookEntity();
        domain = new Book();
    }

    @Test
    @DisplayName("Should get all books paginated")
    void getAllBooks() {
        when(repository.findAllPaginated(0, 10)).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Book.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAllBooks(0, 10))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get books by gender with like format")
    void getAllBooksByGender() {
        when(repository.findByGender("%Action%", 0, 10)).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Book.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAllBooksByGender(0, 10, "Action"))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void countAllByGender() {
        when(repository.countByGender("%Action%")).thenReturn(Mono.just(1L));
        StepVerifier.create(adapter.countAllByGender("Action"))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get books by title with like format")
    void getAllBooksByTitle() {
        when(repository.findByTitle("%Title%", 0, 10)).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Book.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAllBooksByTitle(0, 10, "Title"))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void countAllByTitle() {
        when(repository.countByTitle("%Title%")).thenReturn(Mono.just(1L));
        StepVerifier.create(adapter.countAllByTitle("Title"))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void getAllBooksByLanguage() {
        when(repository.findByLanguage("%ES%", 0, 10)).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Book.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAllBooksByLanguage(0, 10, "ES"))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void countAllByLanguage() {
        when(repository.countByLanguage("%ES%")).thenReturn(Mono.just(1L));
        StepVerifier.create(adapter.countAllByLanguage("ES"))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void getAllBooksByFormat() {
        Format format = Format.DIGITAL;
        when(repository.findByFormat(format.name(), 0, 10)).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Book.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAllBooksByFormat(0, 10, format))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void countAllByFormat() {
        Format format = Format.DIGITAL;
        when(repository.countByFormat(format.name())).thenReturn(Mono.just(1L));
        StepVerifier.create(adapter.countAllByFormat(format))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void getAllBooksByPublisherId() {
        UUID id = UUID.randomUUID();
        when(repository.findByPublisherId(id, 0, 10)).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Book.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAllBooksByPublisherId(0, 10, id))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void countAllByPublisherId() {
        UUID id = UUID.randomUUID();
        when(repository.countByPublisherId(id)).thenReturn(Mono.just(1L));
        StepVerifier.create(adapter.countAllByPublisherId(id))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void getAllBooksByAuthorId() {
        UUID id = UUID.randomUUID();
        when(repository.findByAuthorId(id, 0, 10)).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Book.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAllBooksByAuthorId(0, 10, id))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void countAllByAuthorId() {
        UUID id = UUID.randomUUID();
        when(repository.countByAuthorId(id)).thenReturn(Mono.just(1L));
        StepVerifier.create(adapter.countAllByAuthorId(id))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void getAllBooksByCategoryId() {
        UUID id = UUID.randomUUID();
        when(repository.findByCategoryId(id, 0, 10)).thenReturn(Flux.just(entity));
        when(mapper.map(entity, Book.class)).thenReturn(domain);

        StepVerifier.create(adapter.getAllBooksByCategoryId(0, 10, id))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void countAllByCategoryId() {
        UUID id = UUID.randomUUID();
        when(repository.countByCategoryId(id)).thenReturn(Mono.just(1L));
        StepVerifier.create(adapter.countAllByCategoryId(id))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void validateBookStock() {
        UUID id = UUID.randomUUID();
        when(repository.validateBookStock(id)).thenReturn(Mono.just(true));
        StepVerifier.create(adapter.validateBookStock(id))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    void getBookById() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Book.class)).thenReturn(domain);
        StepVerifier.create(adapter.getBookById(id))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void saveBook() {
        when(mapper.map(domain, BookEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Book.class)).thenReturn(domain);

        StepVerifier.create(adapter.saveBook(domain))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void updateBook() {
        UUID id = UUID.randomUUID();
        when(mapper.map(domain, BookEntity.class)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.map(entity, Book.class)).thenReturn(domain);

        StepVerifier.create(adapter.updateBook(id, domain))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    void countAll() {
        when(repository.countAll()).thenReturn(Mono.just(100L));
        StepVerifier.create(adapter.countAll())
                .expectNext(100L)
                .verifyComplete();
    }
}