package pe.ask.library.usecase.loan;

import pe.ask.library.model.loan.Loan;
import pe.ask.library.model.loan.Status;
import pe.ask.library.port.in.usecase.loan.IChangeLoanStatusUseCase;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistense.ILoanRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@UseCase
public class ChangeLoanStatusUseCase implements IChangeLoanStatusUseCase {

    private final ILoanRepository repository;
    private final IKafkaMessageSenderPort kafkaSender;

    public ChangeLoanStatusUseCase(ILoanRepository repository, IKafkaMessageSenderPort kafkaSender) {
        this.repository = repository;
        this.kafkaSender = kafkaSender;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Loan> changeLoanStatus(UUID loanId, Status status) {
        return repository.getLoanById(loanId)
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .flatMap(loan -> {
                    loan.setStatus(status);
                    UpdateBook updateBookMsg = null;
                    if (status.equals(Status.RETURNED)) {
                        loan.setDateOfRealReturn(LocalDateTime.now());
                        updateBookMsg = new UpdateBook(loan.getBookId(), 1, false);
                    }
                    LoanNotificationMsg notificationMsg = new LoanNotificationMsg(
                            loan.getId(),
                            loan.getUserId(),
                            loan.getLoanDate(),
                            loan.getEstimatedReturnDate(),
                            loan.getStatus()
                    );
                    UpdateBook finalUpdateBookMsg = updateBookMsg;
                    return repository.loanChangeStatus(loanId, loan)
                            .flatMap(savedLoan -> {
                                Mono<Void> sendNotification = kafkaSender.send("loan-notification", notificationMsg);

                                Mono<Void> sendStockUpdate = (finalUpdateBookMsg != null)
                                        ? kafkaSender.send("loan-update-book-stock", finalUpdateBookMsg)
                                        : Mono.empty();
                                return Mono.when(sendNotification, sendStockUpdate)
                                        .thenReturn(savedLoan);
                            });
                });
    }

    public record LoanNotificationMsg(
            UUID loanId,
            String userId,
            LocalDateTime loanDate,
            LocalDateTime estimatedReturnDate,
            Status status
    ){ }

    public record UpdateBook(
            UUID bookId,
            int quantity,
            boolean isIncrement
    ) { }
}