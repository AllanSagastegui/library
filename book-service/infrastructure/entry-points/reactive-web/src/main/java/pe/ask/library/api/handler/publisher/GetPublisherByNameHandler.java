package pe.ask.library.api.handler.publisher;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.response.PublisherResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.publisher.IGetPublisherByNameUseCase;
import reactor.core.publisher.Mono;

@Component
public class GetPublisherByNameHandler extends ReactiveHandlerOperations<
        IGetPublisherByNameUseCase
        > {

    protected GetPublisherByNameHandler(IGetPublisherByNameUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenGETPublisherByNameUseCase(ServerRequest serverRequest) {
        int page = getPage(serverRequest);
        int size = getSize(serverRequest);
        String name = getPathVar(serverRequest, "name");

        return buildPagedResponse(
                useCase.getPublishersByName(page, size, name),
                PublisherResponse.class
        );
    }
}
