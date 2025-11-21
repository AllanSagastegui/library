package pe.ask.library.port.in.usecase.book;

import pe.ask.library.model.book.BookComplete;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IGetAllBooksByLanguageUseCase {
    Mono<Pageable<BookComplete>> getAllBooksByLanguage(int page, int size, String language);
}
