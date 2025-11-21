package pe.ask.library.api.handler.book;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.book.MapBookComplete;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.book.IGetAllBooksByAuthorIdUseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class GetAllBooksByAuthorIdHandler extends ReactiveHandlerOperations<
        IGetAllBooksByAuthorIdUseCase
        > {

    private final MapBookComplete mapBookComplete;

    protected GetAllBooksByAuthorIdHandler(
            IGetAllBooksByAuthorIdUseCase useCase,
            ObjectMapper mapper,
            CustomValidator validator,
            MapBookComplete mapBookComplete
    ) {
        super(useCase, mapper, validator);
        this.mapBookComplete = mapBookComplete;
    }

    public Mono<ServerResponse> listenGETAllBooksByAuthorIdUseCase(ServerRequest serverRequest) {
        int page = getPage(serverRequest);
        int size = getSize(serverRequest);
        UUID authorId = getPathUuid(serverRequest, "authorId");

        return useCase.getAllBooksByAuthorId(page, size, authorId)
                .map(mapBookComplete::toResponse)
                .flatMap(response ->
                        buildResponse(Mono.just(response))
                );
    }
}
