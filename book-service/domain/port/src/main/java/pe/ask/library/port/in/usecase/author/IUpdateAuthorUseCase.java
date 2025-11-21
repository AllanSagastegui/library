package pe.ask.library.port.in.usecase.author;

import pe.ask.library.model.author.Author;
import reactor.core.publisher.Mono;

import java.util.UUID;

@FunctionalInterface
public interface IUpdateAuthorUseCase {
    Mono<Author> updateAuthor(UUID id, Author author);
}
