package pe.ask.library.usecase.loan;

import pe.ask.library.model.loan.Loan;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.loan.IGetAllLoansUseCase;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistense.ILoanRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

@UseCase
public class GetAllLoansUseCase implements IGetAllLoansUseCase {

    private final ILoanRepository repository;
    private final IPaginationUtils utils;

    public GetAllLoansUseCase(ILoanRepository repository, IPaginationUtils utils) {
        this.repository = repository;
        this.utils = utils;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Pageable<Loan>> getAllLoans(int page, int size) {
        return utils.createPageable(
                repository.getAllLoans(page, size),
                repository.countAll(),
                page,
                size
        );
    }
}
