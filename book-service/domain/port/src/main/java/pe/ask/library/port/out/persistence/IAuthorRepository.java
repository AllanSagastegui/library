package pe.ask.library.port.out.persistence;

import pe.ask.library.model.author.Author;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IAuthorRepository {
    Flux<Author> getAllAuthors(int page, int size);
    Flux<Author> getAllAuthorsByNationality(int page, int size, String nationality);
    Mono<Author> getAuthorById(UUID id);
    Mono<Author> getAuthorByPseudonym(String pseudonym);
    Mono<Author> saveAuthor(Author author);
    Mono<Author> updateAuthor(UUID id, Author author);
    Mono<Long> countAll();
    Mono<Long> countAllByNationality(String nationality);
}
