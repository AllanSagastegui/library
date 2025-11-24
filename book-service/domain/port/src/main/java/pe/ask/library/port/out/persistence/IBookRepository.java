package pe.ask.library.port.out.persistence;

import pe.ask.library.model.book.Book;
import pe.ask.library.model.book.Format;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IBookRepository {
    Flux<Book> getAllBooks(int page, int size);
    Mono<Book> getBookById(UUID id);
    Flux<Book> getAllBooksByTitle(int page, int size, String title);
    Flux<Book> getAllBooksByGender(int page, int size, String gender);
    Flux<Book> getAllBooksByLanguage(int page, int size, String language);
    Flux<Book> getAllBooksByFormat(int page, int size, Format format);
    Flux<Book> getAllBooksByPublisherId(int page, int size, UUID publisherId);
    Flux<Book> getAllBooksByCategoryId(int page, int size, UUID categoryId);
    Flux<Book> getAllBooksByAuthorId(int page, int size, UUID authorId);
    Mono<Book> saveBook(Book book);
    Mono<Book> updateBook(UUID id, Book book);
    Mono<Long> countAll();
    Mono<Long> countAllByTitle(String title);
    Mono<Long> countAllByGender(String gender);
    Mono<Long> countAllByLanguage(String language);
    Mono<Long> countAllByFormat(Format format);
    Mono<Long> countAllByPublisherId(UUID publisherId);
    Mono<Long> countAllByCategoryId(UUID categoryId);
    Mono<Long> countAllByAuthorId(UUID authorId);
    Mono<Boolean> validateBookStock(UUID id);
}
