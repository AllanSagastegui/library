package pe.ask.library.usecase.author;

import pe.ask.library.model.author.Author;
import pe.ask.library.port.in.usecase.author.IGetAuthorByPseudonymUseCase;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IAuthorRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

@UseCase
public class GetAuthorByPseudonymUseCase implements IGetAuthorByPseudonymUseCase {

    private final IAuthorRepository repository;

    public GetAuthorByPseudonymUseCase(IAuthorRepository repository) {
        this.repository = repository;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Author> getAuthorByPseudonym(String pseudonym) {
        return repository.getAuthorByPseudonym(pseudonym)
                .switchIfEmpty(Mono.error(RuntimeException::new));
    }
}
