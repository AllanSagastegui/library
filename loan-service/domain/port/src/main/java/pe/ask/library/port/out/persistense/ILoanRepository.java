package pe.ask.library.port.out.persistense;

import pe.ask.library.model.loan.Loan;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ILoanRepository {
    Mono<Loan> createLoan(Loan loan);
    Mono<Loan> loanChangeStatus(UUID loanId, Loan loan);
    Flux<Loan> getAllLoans(int page, int size);
    Flux<Loan> getAllLoansByUserId(UUID userId, int page, int size);
    Flux<Loan> getAllLoansByBookId(UUID bookId, int page, int size);
    Mono<Loan> getLoanById(UUID loanId);
    Mono<Long> countAll();
    Mono<Long> countAllByUserId(UUID userId);
    Mono<Long> countAllByBookId(UUID bookId);
}
