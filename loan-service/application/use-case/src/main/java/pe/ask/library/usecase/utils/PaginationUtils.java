package pe.ask.library.usecase.utils;

import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@UseCase
public class PaginationUtils implements IPaginationUtils {

    @Override
    public <T> Mono<Pageable<T>> createPageable(Flux<T> contentFlux, Mono<Long> countMono, int page, int size) {
        return contentFlux.collectList()
                .zipWith(countMono)
                .map(tuple -> {
                    List<T> content = tuple.getT1();
                    Long totalElements = tuple.getT2();
                    int totalPages = (int) Math.ceil((double) totalElements / size);

                    return Pageable.<T>builder()
                            .page(page)
                            .size(size)
                            .totalElements(totalElements)
                            .totalPages(totalPages)
                            .content(content)
                            .build();
                });
    }
}
