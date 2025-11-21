package pe.ask.library.api.handler.category;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.response.CategoryResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.category.IGetAllCategoriesUseCase;
import reactor.core.publisher.Mono;

@Component
public class GetAllCategoriesHandler extends ReactiveHandlerOperations<
        IGetAllCategoriesUseCase
        > {

    protected GetAllCategoriesHandler(IGetAllCategoriesUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenGETAllCategoriesUseCase(ServerRequest serverRequest) {
        int page = getPage(serverRequest);
        int size = getSize(serverRequest);

        return buildPagedResponse(
                useCase.getAllCategories(page, size),
                CategoryResponse.class
        );
    }
}
