package pe.ask.library.usecase.author;

import pe.ask.library.model.author.Author;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.author.IGetAllAuthorsUseCase;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IAuthorRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

@UseCase
public class GetAllAuthorsUseCase implements IGetAllAuthorsUseCase {

    private final IAuthorRepository repository;
    private final IPaginationUtils utils;

    public GetAllAuthorsUseCase(IAuthorRepository repository, IPaginationUtils utils) {
        this.repository = repository;
        this.utils = utils;
    }
    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Pageable<Author>> getAllAuthors(int page, int size) {
        return utils.createPageable(
                repository.getAllAuthors(page, size),
                repository.countAll(),
                page,
                size
        );
    }
}
