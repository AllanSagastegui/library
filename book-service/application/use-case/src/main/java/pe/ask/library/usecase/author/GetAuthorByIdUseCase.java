package pe.ask.library.usecase.author;

import pe.ask.library.model.author.Author;
import pe.ask.library.port.in.usecase.author.IGetAuthorByIdUseCase;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IAuthorRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@UseCase
public class GetAuthorByIdUseCase implements IGetAuthorByIdUseCase {

    private final IAuthorRepository repository;

    public GetAuthorByIdUseCase(IAuthorRepository repository) {
        this.repository = repository;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Author> getAuthorById(UUID id) {
        return repository.getAuthorById(id)
                .switchIfEmpty(Mono.error(RuntimeException::new));
    }
}
