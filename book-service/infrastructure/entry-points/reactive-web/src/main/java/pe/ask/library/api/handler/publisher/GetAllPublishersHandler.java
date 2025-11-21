package pe.ask.library.api.handler.publisher;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.response.PublisherResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.publisher.IGetAllPublishersUseCase;
import reactor.core.publisher.Mono;

@Component
public class GetAllPublishersHandler extends ReactiveHandlerOperations<
        IGetAllPublishersUseCase
        > {

    protected GetAllPublishersHandler(IGetAllPublishersUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenGETAllPublishersUseCase(ServerRequest serverRequest) {
        int page = getPage(serverRequest);
        int size = getSize(serverRequest);
        return buildPagedResponse(
                useCase.getAllPublishers(page, size),
                PublisherResponse.class
        );
    }
}
