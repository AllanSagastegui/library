package pe.ask.library.persistence.adapter;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import pe.ask.library.model.loan.Loan;
import pe.ask.library.persistence.entity.LoanEntity;
import pe.ask.library.persistence.helper.ReactiveAdapterOperations;
import pe.ask.library.persistence.repository.ILoanReactiveRepository;
import pe.ask.library.port.out.persistense.ILoanRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class LoanReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Loan,
        LoanEntity,
        UUID,
        ILoanReactiveRepository
        > implements ILoanRepository {

    public LoanReactiveRepositoryAdapter(ILoanReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Loan.class));
    }

    private String like(String value) {
        return "%" + value + "%";
    }

    @Override
    public Mono<Loan> createLoan(Loan loan) {
        return super.repository.save(toData(loan))
                .map(this::toEntity);
    }

    @Override
    public Mono<Loan> loanChangeStatus(UUID loanId, Loan loan) {
        loan.setId(loanId);
        return super.repository.save(toData(loan))
                .map(this::toEntity);
    }

    @Override
    public Flux<Loan> getAllLoans(int page, int size) {
        return super.repository.findAllPaginated(page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Flux<Loan> getAllLoansByUserId(UUID userId, int page, int size) {
        return super.repository.getAllLoansByUserId(userId, page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Flux<Loan> getAllLoansByBookId(UUID bookId, int page, int size) {
        return super.repository.getAllLoansByUserId(bookId, page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Mono<Loan> getLoanById(UUID loanId) {
        return super.repository.findById(loanId)
                .map(this::toEntity);
    }

    @Override
    public Mono<Long> countAll() {
        return super.repository.countAll();
    }

    @Override
    public Mono<Long> countAllByUserId(UUID userId) {
        return super.repository.countAllByUserId(userId);
    }

    @Override
    public Mono<Long> countAllByBookId(UUID bookId) {
        return super.repository.countAllByUserId(bookId);
    }
}
