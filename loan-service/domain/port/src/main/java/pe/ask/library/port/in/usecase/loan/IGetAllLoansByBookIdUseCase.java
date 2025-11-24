package pe.ask.library.port.in.usecase.loan;

import pe.ask.library.model.loan.Loan;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

import java.util.UUID;

@FunctionalInterface
public interface IGetAllLoansByBookIdUseCase {
    Mono<Pageable<Loan>> getAllLoansByBookId(UUID bookId, int page, int size);
}
