package pe.ask.library.api.handler.author;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.response.AuthorResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.author.IGetAllAuthorsUseCase;
import reactor.core.publisher.Mono;

@Component
public class GetAllAuthorsHandler extends ReactiveHandlerOperations<
        IGetAllAuthorsUseCase
        > {

    protected GetAllAuthorsHandler(IGetAllAuthorsUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenGETAllAuthorsUseCase(ServerRequest serverRequest) {
        int page = getPage(serverRequest);
        int size = getSize(serverRequest);

        return buildPagedResponse(
                useCase.getAllAuthors(page, size),
                AuthorResponse.class
        );
    }
}
