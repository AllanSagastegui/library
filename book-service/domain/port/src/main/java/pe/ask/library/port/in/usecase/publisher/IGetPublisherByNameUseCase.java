package pe.ask.library.port.in.usecase.publisher;

import pe.ask.library.model.publisher.Publisher;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IGetPublisherByNameUseCase {
    Mono<Pageable<Publisher>>  getPublishersByName(int page, int size, String name);
}
