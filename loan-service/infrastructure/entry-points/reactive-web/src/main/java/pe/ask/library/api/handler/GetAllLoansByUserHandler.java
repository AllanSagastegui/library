package pe.ask.library.api.handler;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.response.LoanResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.loan.IGetAllLoansByUserIdUseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class GetAllLoansByUserHandler extends ReactiveHandlerOperations<
        IGetAllLoansByUserIdUseCase
        > {


    protected GetAllLoansByUserHandler(IGetAllLoansByUserIdUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenGETAllLoansByUserIdUseCase(ServerRequest serverRequest) {
        int page = getPage(serverRequest);
        int size = getSize(serverRequest);
        UUID userId = getPathUuid(serverRequest, "userId");

        return useCase.getAllLoansByUserId(userId, page, size)
                .flatMap(response ->
                        buildPagedResponse(
                                Mono.just(response), LoanResponse.class
                        )
                );
    }
}
