package pe.ask.library.usecase.publisher;

import pe.ask.library.model.publisher.Publisher;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.publisher.IGetPublisherByNameUseCase;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IPublisherRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

@UseCase
public class GetPublisherByNameUseCase implements IGetPublisherByNameUseCase {

    private final IPublisherRepository repository;
    private final IPaginationUtils utils;

    public  GetPublisherByNameUseCase(IPublisherRepository repository, IPaginationUtils utils) {
        this.repository = repository;
        this.utils = utils;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Pageable<Publisher>> getPublishersByName(int page, int size, String name) {
        return utils.createPageable(
                repository.getPublishersByName(page, size, name),
                repository.countAllByName(name),
                page,
                size
        );
    }
}
