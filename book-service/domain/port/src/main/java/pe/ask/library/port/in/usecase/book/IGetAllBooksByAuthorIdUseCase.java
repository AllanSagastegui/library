package pe.ask.library.port.in.usecase.book;

import pe.ask.library.model.book.BookComplete;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

import java.util.UUID;

@FunctionalInterface
public interface IGetAllBooksByAuthorIdUseCase {
    Mono<Pageable<BookComplete>> getAllBooksByAuthorId(int page, int size, UUID authorId);
}
