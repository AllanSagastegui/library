package pe.ask.library.api.handler.book;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.book.MapBookComplete;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.model.book.Format;
import pe.ask.library.port.in.usecase.book.IGetAllBooksByFormatUseCase;
import reactor.core.publisher.Mono;

@Component
public class GetAllBooksByFormatHandler extends ReactiveHandlerOperations<
        IGetAllBooksByFormatUseCase
        > {

    private final MapBookComplete mapBookComplete;

    protected GetAllBooksByFormatHandler(
            IGetAllBooksByFormatUseCase useCase,
            ObjectMapper mapper,
            CustomValidator validator,
            MapBookComplete mapBookComplete
    ) {
        super(useCase, mapper, validator);
        this.mapBookComplete = mapBookComplete;
    }

    public Mono<ServerResponse> listenGETAllBooksByFormatUseCase(ServerRequest serverRequest) {
        int page = getPage(serverRequest);
        int size = getSize(serverRequest);
        String format = getPathVar(serverRequest, "format");

        return useCase.getAllBooksByFormat(page, size, Format.valueOf(format.toUpperCase()))
                .map(mapBookComplete::toResponse)
                .flatMap(response ->
                        buildResponse(Mono.just(response))
                );
    }
}
