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
import pe.ask.library.port.in.usecase.author.IUpdateAuthorUseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class UpdateAuthorHandler extends ReactiveHandlerOperations<
        IUpdateAuthorUseCase
        > {

    protected UpdateAuthorHandler(IUpdateAuthorUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenPUTUpdateAuthorUseCase(ServerRequest serverRequest) {
        UUID id = getPathUuid(serverRequest, "id");
        return extractBodyToDomain(serverRequest, AuthorRequest.class, Author.class)
                .flatMap(author -> useCase.updateAuthor(id, author))
                .map(author -> map(author, AuthorResponse.class))
                .flatMap(response ->
                        buildResponse(Mono.just(response))
                );
    }
}
