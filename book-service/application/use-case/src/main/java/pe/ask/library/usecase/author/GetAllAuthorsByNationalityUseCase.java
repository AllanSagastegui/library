package pe.ask.library.usecase.author;

import pe.ask.library.model.author.Author;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.author.IGetAllAuthorsByNationalityUseCase;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IAuthorRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

@UseCase
public class GetAllAuthorsByNationalityUseCase implements IGetAllAuthorsByNationalityUseCase {

    private final IAuthorRepository repository;
    private final IPaginationUtils utils;

    public GetAllAuthorsByNationalityUseCase(IAuthorRepository repository, IPaginationUtils utils) {
        this.repository = repository;
        this.utils = utils;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Pageable<Author>> getAllAuthorsByNationality(int page, int size, String nationality) {
        return utils.createPageable(
                repository.getAllAuthorsByNationality(page, size, nationality),
                repository.countAllByNationality(nationality),
                page,
                size
        );
    }
}
