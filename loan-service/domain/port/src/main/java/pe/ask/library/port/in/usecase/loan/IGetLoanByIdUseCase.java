package pe.ask.library.port.in.usecase.loan;

import pe.ask.library.model.loan.Loan;
import reactor.core.publisher.Mono;

import java.util.UUID;

@FunctionalInterface
public interface IGetLoanByIdUseCase {
    Mono<Loan> getLoanById(UUID loanId);
}
