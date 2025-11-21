package pe.ask.library.port.in.usecase.author;

import pe.ask.library.model.author.Author;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IGetAuthorByPseudonymUseCase {
    Mono<Author> getAuthorByPseudonym(String pseudonym);
}
