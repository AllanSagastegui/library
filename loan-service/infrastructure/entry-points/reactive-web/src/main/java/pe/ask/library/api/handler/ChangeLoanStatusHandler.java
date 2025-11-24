package pe.ask.library.api.handler;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.dto.request.ChangeStatusRequest;
import pe.ask.library.api.dto.response.LoanResponse;
import pe.ask.library.api.helper.ReactiveHandlerOperations;
import pe.ask.library.api.utils.validator.CustomValidator;
import pe.ask.library.port.in.usecase.loan.IChangeLoanStatusUseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class ChangeLoanStatusHandler extends ReactiveHandlerOperations<
        IChangeLoanStatusUseCase
        > {

    protected ChangeLoanStatusHandler(IChangeLoanStatusUseCase useCase, ObjectMapper mapper, CustomValidator validator) {
        super(useCase, mapper, validator);
    }

    public Mono<ServerResponse> listenPATCHChangeLoanStatusUseCase(ServerRequest serverRequest) {
        UUID id = getPathUuid(serverRequest, "loanId");

        return extractBody(serverRequest, ChangeStatusRequest.class)
                .flatMap(payload -> useCase.changeLoanStatus(id, payload.getStatus()))
                .map(loanDomain -> map(loanDomain, LoanResponse.class))
                .flatMap(response ->
                        buildResponse(Mono.just(response))
                );
    }
}
