package pe.ask.library.port.in.usecase.loan;

import pe.ask.library.model.loan.Loan;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface ICreateLoanUseCase {
    Mono<Loan> createLoan(Loan loan);
}
