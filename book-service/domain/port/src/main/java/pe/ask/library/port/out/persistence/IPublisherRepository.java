package pe.ask.library.port.out.persistence;

import pe.ask.library.model.publisher.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IPublisherRepository {
    Flux<Publisher> getAllPublishers(int page, int size);
    Mono<Publisher> getPublisherById(UUID publisherId);
    Flux<Publisher> getPublishersByName(int page, int size, String name);
    Flux<Publisher> getAllPublishersByCountry(int page, int size, String country);
    Mono<Long> countAll();
    Mono<Long> countAllByName(String name);
    Mono<Long> countAllByCountry(String country);
}
