package pe.ask.library.api.handler.book;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.book.MapBookComplete;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.book.IGetAllBooksByLanguageUseCase;
import reactor.core.publisher.Mono;

@Component
public class GetAllBooksByLanguageHandler extends ReactiveHandlerOperations<
        IGetAllBooksByLanguageUseCase
        > {

    private final MapBookComplete mapBookComplete;

    protected GetAllBooksByLanguageHandler(
            IGetAllBooksByLanguageUseCase useCase,
            ObjectMapper mapper,
            CustomValidator validator,
            MapBookComplete mapBookComplete
    ) {
        super(useCase, mapper, validator);
        this.mapBookComplete = mapBookComplete;
    }

    public Mono<ServerResponse> listenGETAllBooksByLanguageUseCase(ServerRequest serverRequest) {
        int page = getPage(serverRequest);
        int size = getSize(serverRequest);
        String language = getPathVar(serverRequest, "language");

        return useCase.getAllBooksByLanguage(page, size, language)
                .map(mapBookComplete::toResponse)
                .flatMap(response ->
                        buildResponse(Mono.just(response))
                );
    }
}
