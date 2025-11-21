package pe.ask.library.port.in.usecase.book;

import pe.ask.library.model.book.Book;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ISaveBookUseCase {
    Mono<Book> saveBook(Book book);
}
