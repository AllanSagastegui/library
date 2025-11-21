package pe.ask.library.usecase.author;

import pe.ask.library.model.author.Author;
import pe.ask.library.port.in.usecase.author.ISaveAuthorUseCase;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IAuthorRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

@UseCase
public class SaveAuthorUseCase implements ISaveAuthorUseCase {

    private final IAuthorRepository repository;

    public SaveAuthorUseCase(IAuthorRepository repository) {
        this.repository = repository;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Author> saveAuthor(Author author) {
        return validateUniqueField(author)
                .then(repository.saveAuthor(author));
    }

    private Mono<Void> validateUniqueField(Author author) {
        return repository.getAuthorByPseudonym(author.getPseudonym())
                .flatMap(found -> Mono.<Void>error(RuntimeException::new))
                .then();
    }
}
