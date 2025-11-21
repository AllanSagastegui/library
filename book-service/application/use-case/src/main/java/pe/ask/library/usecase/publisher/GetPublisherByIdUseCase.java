package pe.ask.library.usecase.publisher;

import pe.ask.library.model.publisher.Publisher;
import pe.ask.library.port.in.usecase.publisher.IGetPublisherByIdUseCase;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IPublisherRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@UseCase
public class GetPublisherByIdUseCase implements IGetPublisherByIdUseCase {

    private final IPublisherRepository repository;

    public  GetPublisherByIdUseCase(IPublisherRepository repository) {
        this.repository = repository;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Publisher> getPublisherById(UUID publisherId) {
        return repository.getPublisherById(publisherId)
                .switchIfEmpty(Mono.error(RuntimeException::new));
    }
}
