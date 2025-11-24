package pe.ask.library.usecase.loan;

import pe.ask.library.model.loan.Loan;
import pe.ask.library.model.utils.Pageable;
import pe.ask.library.port.in.usecase.loan.IGetAllLoansByBookIdUseCase;
import pe.ask.library.port.in.usecase.utils.IPaginationUtils;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistense.ILoanRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@UseCase
public class GetAllLoansByBookIdUseCase implements IGetAllLoansByBookIdUseCase {

    private final ILoanRepository repository;
    private final IPaginationUtils utils;

    public GetAllLoansByBookIdUseCase(ILoanRepository repository, IPaginationUtils utils) {
        this.repository = repository;
        this.utils = utils;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Pageable<Loan>> getAllLoansByBookId(UUID bookId, int page, int size) {
        return utils.createPageable(
                repository.getAllLoansByBookId(bookId, page, size),
                repository.countAllByBookId(bookId),
                page,
                size
        );
    }
}
