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
import pe.ask.library.port.in.usecase.book.IUpdateBookUseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class UpdateBookHandler extends ReactiveHandlerOperations<
        IUpdateBookUseCase
        > {

    protected UpdateBookHandler(IUpdateBookUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenPUTUpdateBookUseCase(ServerRequest serverRequest) {
        UUID id = getPathUuid(serverRequest, "id");
        return extractBodyToDomain(serverRequest, BookRequest.class, Book.class)
                .flatMap(book -> useCase.updateBook(id, book))
                .map(book -> map(book, BookResponse.class))
                .flatMap(response ->
                        buildResponse(Mono.just(response))
                );
    }
}
