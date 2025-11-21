package pe.ask.library.usecase.author;

import pe.ask.library.model.author.Author;
import pe.ask.library.port.in.usecase.author.IUpdateAuthorUseCase;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IAuthorRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@UseCase
public class UpdateAuthorUseCase implements IUpdateAuthorUseCase {

    private final IAuthorRepository repository;

    public UpdateAuthorUseCase(IAuthorRepository repository) {
        this.repository = repository;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Author> updateAuthor(UUID id, Author author) {
        return repository.getAuthorById(id)
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .flatMap(existing ->
                        validateUniqueField(id, author)
                                .then(repository.updateAuthor(id, author))
                );
    }

    private Mono<Void> validateUniqueField(UUID id, Author author) {
        return repository.getAuthorByPseudonym(author.getPseudonym())
                .filter(a -> !a.getId().equals(id))
                .flatMap(a -> Mono.error(RuntimeException::new))
                .then();
    }
}
