package pe.ask.library.api.handler.author;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.request.AuthorRequest;
import pe.ask.library.api.dto.response.AuthorResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.model.author.Author;
import pe.ask.library.port.in.usecase.author.ISaveAuthorUseCase;
import reactor.core.publisher.Mono;

@Component
public class SaveAuthorHandler extends ReactiveHandlerOperations<
        ISaveAuthorUseCase
        > {

    protected SaveAuthorHandler(ISaveAuthorUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenPOSTSaveAuthorUseCase(ServerRequest serverRequest) {
        return extractBodyToDomain(serverRequest, AuthorRequest.class, Author.class)
                .flatMap(useCase::saveAuthor)
                .flatMap(response -> buildCreatedResponse(
                        map(response, AuthorResponse.class),
                        createUri(serverRequest, "/{id}", response.getId().toString())
                ));
    }
}
