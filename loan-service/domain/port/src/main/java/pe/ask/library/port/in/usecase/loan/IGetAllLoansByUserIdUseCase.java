package pe.ask.library.port.in.usecase.loan;

import pe.ask.library.model.loan.Loan;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;


@FunctionalInterface
public interface IGetAllLoansByUserIdUseCase {
    Mono<Pageable<Loan>> getAllLoansByUserId(String userId, int page, int size);
}
