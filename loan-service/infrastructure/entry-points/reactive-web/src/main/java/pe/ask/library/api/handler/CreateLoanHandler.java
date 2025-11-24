package pe.ask.library.api.handler;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.request.LoanRequest;
import pe.ask.library.api.dto.response.LoanResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.model.loan.Loan;
import pe.ask.library.port.in.usecase.loan.ICreateLoanUseCase;
import reactor.core.publisher.Mono;

@Component
public class CreateLoanHandler extends ReactiveHandlerOperations<
        ICreateLoanUseCase
        > {

    protected CreateLoanHandler(ICreateLoanUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenPOSTCreateLoanUseCase(ServerRequest serverRequest) {
        return extractBodyToDomain(serverRequest, LoanRequest.class, Loan.class)
                .flatMap(useCase::createLoan)
                .flatMap(response -> buildCreatedResponse(
                        map(response, LoanResponse.class),
                        createUri(serverRequest, "/{id}", response.getId().toString())
                ));
    }
}
