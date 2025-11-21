package pe.ask.library.api.handler.book;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.request.BookRequest;
import pe.ask.library.api.dto.response.BookResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.model.book.Book;
import pe.ask.library.port.in.usecase.book.ISaveBookUseCase;
import reactor.core.publisher.Mono;

@Component
public class SaveBookHandler extends ReactiveHandlerOperations<
        ISaveBookUseCase
        > {

    protected SaveBookHandler(ISaveBookUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenPOSTSaveBookUseCase(ServerRequest serverRequest) {
        return extractBodyToDomain(serverRequest, BookRequest.class, Book.class)
                .flatMap(useCase::saveBook)
                .flatMap(response -> buildCreatedResponse(
                        map(response, BookResponse.class),
                        createUri(serverRequest, "/{id}", response.getId().toString())
                ));
    }
}
