package pe.ask.library.port.in.usecase.loan;

import pe.ask.library.model.loan.Loan;
import pe.ask.library.model.loan.Status;
import reactor.core.publisher.Mono;

import java.util.UUID;

@FunctionalInterface
public interface IChangeLoanStatusUseCase {
    Mono<Loan> changeLoanStatus(UUID loanId, Status status);
}
