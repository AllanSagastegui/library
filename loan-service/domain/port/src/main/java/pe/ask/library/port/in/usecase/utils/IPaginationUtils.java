package pe.ask.library.port.in.usecase.utils;

import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IPaginationUtils {
    <T> Mono<Pageable<T>> createPageable(Flux<T> contentFlux, Mono<Long> countMono, int page, int size);
}
