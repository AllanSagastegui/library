package pe.ask.library.api.handler.publisher;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.response.PublisherResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.publisher.IGetPublisherByCountryUseCase;
import reactor.core.publisher.Mono;

@Component
public class GetPublisherByCountryHandler extends ReactiveHandlerOperations<
        IGetPublisherByCountryUseCase
        > {

    protected GetPublisherByCountryHandler(IGetPublisherByCountryUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenGETPublishersByCountryUseCase(ServerRequest serverRequest) {
        int page = getPage(serverRequest);
        int size = getSize(serverRequest);
        String country = getPathVar(serverRequest, "country");

        return buildPagedResponse(
                useCase.getPublisherByCountry(page, size, country),
                PublisherResponse.class
        );
    }
}
