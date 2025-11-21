package pe.ask.library.port.in.usecase.author;

import pe.ask.library.model.author.Author;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IGetAllAuthorsUseCase {
    Mono<Pageable<Author>> getAllAuthors(int page, int size);
}
