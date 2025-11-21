package pe.ask.library.persistence.adapter;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import pe.ask.library.model.book.Book;
import pe.ask.library.model.book.Format;
import pe.ask.library.persistence.entity.BookEntity;
import pe.ask.library.persistence.helper.ReactiveAdapterOperations;
import pe.ask.library.persistence.repository.IBookReactiveRepository;
import pe.ask.library.port.out.persistence.IBookRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class BookReactiveRepositoryAdapter extends ReactiveAdapterOperations<Book, BookEntity, UUID, IBookReactiveRepository> implements IBookRepository {

    public BookReactiveRepositoryAdapter(IBookReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Book.class));
    }

    private String like(String value) {
        return "%" + value + "%";
    }

    @Override
    public Flux<Book> getAllBooks(int page, int size) {
        return repository.findAllPaginated(page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Flux<Book> getAllBooksByGender(int page, int size, String gender) {
        return repository.findByGender(like(gender), page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Mono<Long> countAllByGender(String gender) {
        return repository.countByGender(like(gender));
    }

    @Override
    public Flux<Book> getAllBooksByTitle(int page, int size, String title) {
        return repository.findByTitle(like(title), page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Mono<Long> countAllByTitle(String title) {
        return repository.countByTitle(like(title));
    }

    @Override
    public Flux<Book> getAllBooksByLanguage(int page, int size, String language) {
        return repository.findByLanguage(like(language), page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Mono<Long> countAllByLanguage(String language) {
        return repository.countByLanguage(like(language));
    }

    @Override
    public Flux<Book> getAllBooksByFormat(int page, int size, Format format) {
        return repository.findByFormat(format.name(), page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Mono<Long> countAllByFormat(Format format) {
        return repository.countByFormat(format.name());
    }

    @Override
    public Flux<Book> getAllBooksByPublisherId(int page, int size, UUID publisherId) {
        return repository.findByPublisherId(publisherId, page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Mono<Long> countAllByPublisherId(UUID publisherId) {
        return repository.countByPublisherId(publisherId);
    }

    @Override
    public Flux<Book> getAllBooksByAuthorId(int page, int size, UUID authorId) {
        return repository.findByAuthorId(authorId, page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Mono<Long> countAllByAuthorId(UUID authorId) {
        return repository.countByAuthorId(authorId);
    }

    @Override
    public Flux<Book> getAllBooksByCategoryId(int page, int size, UUID categoryId) {
        return repository.findByCategoryId(categoryId, page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Mono<Long> countAllByCategoryId(UUID categoryId) {
        return repository.countByCategoryId(categoryId);
    }

    @Override
    public Mono<Book> getBookById(UUID id) {
        return super.repository.findById(id).map(this::toEntity);
    }
    @Override
    public Mono<Book> saveBook(Book book) {
        return super.repository.save(toData(book)).map(this::toEntity);
    }
    @Override
    public Mono<Book> updateBook(UUID id, Book book) {
        book.setId(id);
        return super.repository.save(toData(book)).map(this::toEntity);
    }
    @Override
    public Mono<Long> countAll() {
        return repository.countAll();
    }
}