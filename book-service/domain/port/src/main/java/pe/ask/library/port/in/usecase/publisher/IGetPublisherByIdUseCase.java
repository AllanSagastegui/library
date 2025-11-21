package pe.ask.library.port.in.usecase.publisher;

import pe.ask.library.model.publisher.Publisher;
import reactor.core.publisher.Mono;

import java.util.UUID;

@FunctionalInterface
public interface IGetPublisherByIdUseCase {
    Mono<Publisher> getPublisherById(UUID publisherId);
}
