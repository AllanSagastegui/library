package pe.ask.library.api.handler.publisher;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.response.PublisherResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.publisher.IGetPublisherByIdUseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class GetPublisherByIdHandler extends ReactiveHandlerOperations<
        IGetPublisherByIdUseCase
        > {

    protected GetPublisherByIdHandler(IGetPublisherByIdUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenGETPublisherByIdUseCase(ServerRequest serverRequest) {
        UUID id = getPathUuid(serverRequest, "id");

        return useCase.getPublisherById(id)
                .map(publisher -> map(publisher, PublisherResponse.class))
                .flatMap(response ->
                        buildResponse(Mono.just(response))
                );
    }
}
