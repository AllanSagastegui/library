package pe.ask.library.port.in.usecase.book;

import pe.ask.library.model.book.Book;
import reactor.core.publisher.Mono;

import java.util.UUID;

@FunctionalInterface
public interface IUpdateBookUseCase {
    Mono<Book> updateBook(UUID id, Book book);
}
