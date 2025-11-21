package pe.ask.library.port.in.usecase.book;

import pe.ask.library.model.book.BookComplete;
import pe.ask.library.model.book.Format;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IGetAllBooksByFormatUseCase {
    Mono<Pageable<BookComplete>> getAllBooksByFormat(int page, int size, Format format);
}
