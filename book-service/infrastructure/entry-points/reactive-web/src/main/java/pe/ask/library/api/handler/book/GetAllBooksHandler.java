package pe.ask.library.api.handler.book;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.book.MapBookComplete;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.book.IGetAllBooksUseCase;
import reactor.core.publisher.Mono;

@Component
public class GetAllBooksHandler extends ReactiveHandlerOperations<
        IGetAllBooksUseCase
        > {

    private final MapBookComplete mapBookComplete;

    protected GetAllBooksHandler(
            IGetAllBooksUseCase useCase,
            ObjectMapper mapper,
            CustomValidator validator,
            MapBookComplete mapBookComplete
    ) {
        super(useCase, mapper, validator);
        this.mapBookComplete = mapBookComplete;
    }

    public Mono<ServerResponse> listenGETAllBooksUseCase(ServerRequest serverRequest) {
        int page = getPage(serverRequest);
        int size = getSize(serverRequest);

        return useCase.getAllBooks(page, size)
                .map(mapBookComplete::toResponse)
                .flatMap(response ->
                        buildResponse(Mono.just(response))
                );
    }
}
