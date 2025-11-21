package pe.ask.library.api.handler.book;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.book.MapBookComplete;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.book.IGetAllBooksByPublisherIdUseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class GetAllBooksByPublisherIdHandler extends ReactiveHandlerOperations<
        IGetAllBooksByPublisherIdUseCase
        > {

    private final MapBookComplete mapBookComplete;

    protected GetAllBooksByPublisherIdHandler(
            IGetAllBooksByPublisherIdUseCase useCase,
            ObjectMapper mapper,
            CustomValidator validator,
            MapBookComplete mapBookComplete
    ) {
        super(useCase, mapper, validator);
        this.mapBookComplete = mapBookComplete;
    }

    public Mono<ServerResponse> listenGETAllBooksByPublisherIdUseCase(ServerRequest serverRequest) {
        int page = getPage(serverRequest);
        int size = getSize(serverRequest);
        UUID id = getPathUuid(serverRequest, "publisherId");

        return useCase.getAllBooksByPublisherId(page, size, id)
                .map(mapBookComplete::toResponse)
                .flatMap(response ->
                        buildResponse(Mono.just(response))
                );
    }
}
