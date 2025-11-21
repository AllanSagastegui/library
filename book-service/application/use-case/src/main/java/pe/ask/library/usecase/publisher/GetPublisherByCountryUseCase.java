package pe.ask.library.usecase.publisher;

import pe.ask.library.model.publisher.Publisher;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.publisher.IGetPublisherByCountryUseCase;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IPublisherRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

@UseCase
public class GetPublisherByCountryUseCase implements IGetPublisherByCountryUseCase {

    private final IPublisherRepository repository;
    private final IPaginationUtils utils;

    public  GetPublisherByCountryUseCase(IPublisherRepository repository, IPaginationUtils utils) {
        this.repository = repository;
        this.utils = utils;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Pageable<Publisher>> getPublisherByCountry(int page, int size, String country) {
        return utils.createPageable(
                repository.getAllPublishersByCountry(page, size, country),
                repository.countAllByCountry(country),
                page,
                size
        );
    }
}
