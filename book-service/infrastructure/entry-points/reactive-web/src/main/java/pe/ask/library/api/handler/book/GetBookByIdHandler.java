package pe.ask.library.api.handler.book;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.response.BookResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.book.IGetBookByIdUseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class GetBookByIdHandler extends ReactiveHandlerOperations<
        IGetBookByIdUseCase
        > {


    protected GetBookByIdHandler(IGetBookByIdUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenGETBookByIdUseCase(ServerRequest serverRequest) {
        UUID id = getPathUuid(serverRequest, "id");

        return useCase.getBookById(id)
                .map(book -> map(book, BookResponse.class))
                .flatMap(response ->
                        buildResponse(Mono.just(response))
                );
    }
}
