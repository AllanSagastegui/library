package pe.ask.library.port.in.usecase.book;

import pe.ask.library.model.book.Book;
import reactor.core.publisher.Mono;

import java.util.UUID;

@FunctionalInterface
public interface IGetBookByIdUseCase {
    Mono<Book> getBookById(UUID id);
}
