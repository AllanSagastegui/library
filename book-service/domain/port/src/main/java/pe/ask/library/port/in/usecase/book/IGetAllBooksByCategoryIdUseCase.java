package pe.ask.library.port.in.usecase.book;

import pe.ask.library.model.book.BookComplete;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

import java.util.UUID;

@FunctionalInterface
public interface IGetAllBooksByCategoryIdUseCase {
    Mono<Pageable<BookComplete>> getAllBooksByCategoryId(int page, int size, UUID categoryId);
}
