package pe.ask.library.usecase.loan;

import pe.ask.library.model.loan.Loan;
import pe.ask.library.port.in.usecase.loan.IGetLoanByIdUseCase;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistense.ILoanRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.util.UUID;

@UseCase
public class GetLoanByIdUseCase implements IGetLoanByIdUseCase {

    private final ILoanRepository repository;

    public GetLoanByIdUseCase(ILoanRepository repository) {
        this.repository = repository;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Loan> getLoanById(UUID loanId) {
        return repository.getLoanById(loanId)
                .switchIfEmpty(Mono.error(RuntimeException::new));
    }
}
