package pe.ask.library.port.in.usecase.loan;

import pe.ask.library.model.loan.Loan;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IGetAllLoansUseCase {
    Mono<Pageable<Loan>> getAllLoans(int page, int size);
}
