package pe.ask.library.api.handler.author;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.response.AuthorResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.author.IGetAuthorByPseudonymUseCase;
import reactor.core.publisher.Mono;

@Component
public class GetAuthorByPseudonymHandler extends ReactiveHandlerOperations<
        IGetAuthorByPseudonymUseCase
        > {

    protected GetAuthorByPseudonymHandler(IGetAuthorByPseudonymUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenGETAuthorByPseudonymUseCase(ServerRequest serverRequest) {
        String pseudonym = getPathVar(serverRequest, "pseudonym");
        return useCase.getAuthorByPseudonym(pseudonym)
                .map(author -> map(author, AuthorResponse.class))
                .flatMap(response ->
                        buildResponse(Mono.just(response))
                );
    }
}

