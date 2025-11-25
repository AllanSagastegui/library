package pe.ask.library.usecase.loan;

import pe.ask.library.model.loan.Loan;
import pe.ask.library.model.loan.Status;
import pe.ask.library.model.utils.IAuditable;
import pe.ask.library.port.in.usecase.loan.ICreateLoanUseCase;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistense.ILoanRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@UseCase
public class CreateLoanUseCase implements ICreateLoanUseCase {

    private final ILoanRepository repository;
    private final IKafkaMessageSenderPort kafkaSender;
    private final IAuditable auditable;

    public CreateLoanUseCase(ILoanRepository repository, IKafkaMessageSenderPort kafkaSender, IAuditable auditable) {
        this.repository = repository;
        this.kafkaSender = kafkaSender;
        this.auditable = auditable;
    }


    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Loan> createLoan(Loan loan) {
        return auditable.getAuditUserId()
                .map(userIdStr -> loan.toBuilder()
                        .userId(userIdStr)
                        .loanDate(LocalDateTime.now())
                        .estimatedReturnDate(LocalDateTime.now().plusDays(15))
                        .dateOfRealReturn(null)
                        .status(Status.PENDING)
                        .build()
                )
                .flatMap(repository::createLoan)
                .flatMap(loanSaved -> {
                    ValidateStock payload = new ValidateStock(loanSaved.getId() ,loanSaved.getBookId());
                    LoanNotificationMsg msg = new LoanNotificationMsg(loanSaved.getId(), loanSaved.getUserId(), loanSaved.getLoanDate(), loanSaved.getEstimatedReturnDate(), loanSaved.getStatus());

                    Mono<Void> sendValidation = kafkaSender.send("loan-validate-book-stock", payload);
                    Mono<Void> sendNotification = sendValidation
                            .then(kafkaSender.send("loan-notification", msg));

                    return sendNotification
                            .thenReturn(loanSaved);
                });
    }

    public record ValidateStock(
            UUID loanId,
            UUID bookId
    ) { }

    public record LoanNotificationMsg(
            UUID loanId,
            String userId,
            LocalDateTime loanDate,
            LocalDateTime estimatedReturnDate,
            Status status
    ){ }
}