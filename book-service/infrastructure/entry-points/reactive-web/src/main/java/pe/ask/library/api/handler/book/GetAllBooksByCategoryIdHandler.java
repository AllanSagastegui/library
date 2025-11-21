package pe.ask.library.api.handler.book;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.book.MapBookComplete;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.book.IGetAllBooksByCategoryIdUseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class GetAllBooksByCategoryIdHandler extends ReactiveHandlerOperations<
        IGetAllBooksByCategoryIdUseCase
        > {

    private final MapBookComplete mapBookComplete;

    protected GetAllBooksByCategoryIdHandler(
            IGetAllBooksByCategoryIdUseCase useCase,
            ObjectMapper mapper,
            CustomValidator validator,
            MapBookComplete mapBookComplete
    ) {
        super(useCase, mapper, validator);
        this.mapBookComplete = mapBookComplete;
    }

    public Mono<ServerResponse> listenGETAllBooksByCategoryIdUseCase(ServerRequest serverRequest) {
        int page = getPage(serverRequest);
        int size = getSize(serverRequest);
        UUID categoryId = getPathUuid(serverRequest, "categoryId");

        return useCase.getAllBooksByCategoryId(page, size, categoryId)
                .map(mapBookComplete::toResponse)
                .flatMap(response ->
                        buildResponse(Mono.just(response))
                );
    }
}
