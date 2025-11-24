package pe.ask.library.api.handler;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.response.LoanResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.loan.IGetLoanByIdUseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class GetLoanByIdHandler extends ReactiveHandlerOperations<
        IGetLoanByIdUseCase
        > {

    protected GetLoanByIdHandler(IGetLoanByIdUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenGETLoanByIdUseCase(ServerRequest serverRequest) {
        UUID loanId = getPathUuid(serverRequest, "loanId");

        return useCase.getLoanById(loanId)
                .map(loan -> map(loan, LoanResponse.class))
                .flatMap(response ->
                        buildResponse(Mono.just(response))
                );
    }
}
