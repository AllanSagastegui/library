package pe.ask.library.api.handler.author;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.response.AuthorResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.author.IGetAllAuthorsByNationalityUseCase;
import reactor.core.publisher.Mono;

@Component
public class GetAllAuthorsByNationalityHandler extends ReactiveHandlerOperations<
        IGetAllAuthorsByNationalityUseCase
        > {

    protected GetAllAuthorsByNationalityHandler(IGetAllAuthorsByNationalityUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenGETAllAuthorsByNationalityUseCase(ServerRequest serverRequest) {
        int page = getPage(serverRequest);
        int size = getSize(serverRequest);
        String nationality = getPathVar(serverRequest, "nationality");

        return buildPagedResponse(
                useCase.getAllAuthorsByNationality(page, size, nationality),
                AuthorResponse.class
        );
    }
}
