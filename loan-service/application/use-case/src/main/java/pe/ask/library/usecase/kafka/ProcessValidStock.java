package pe.ask.library.usecase.kafka;

import pe.ask.library.model.loan.Status;
import pe.ask.library.port.in.usecase.kafka.IProcessValidStock;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistense.ILoanRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@UseCase
public class ProcessValidStock implements IProcessValidStock {

    private final ILoanRepository repository;
    private final IKafkaMessageSenderPort kafkaSender;

    public ProcessValidStock(ILoanRepository repository, IKafkaMessageSenderPort kafkaSender){
        this.repository = repository;
        this.kafkaSender = kafkaSender;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Void> processValidStock(UUID loanId, UUID bookId, Boolean isValid) {
        return repository.getLoanById(loanId)
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .flatMap(loan -> {
                    if (isValid) {
                        loan.setStatus(Status.LOANED);
                    } else {
                        loan.setStatus(Status.FAILED);
                    }
                    return repository.loanChangeStatus(loanId, loan);
                })
                .flatMap(updatedLoan -> { LoanNotificationMsg loanNotificationMsg = new LoanNotificationMsg(
                        updatedLoan.getId(),
                        updatedLoan.getUserId(),
                        updatedLoan.getLoanDate(),
                        updatedLoan.getEstimatedReturnDate(),
                        updatedLoan.getStatus()
                );
                    UpdateBook updateBook = new UpdateBook(bookId, 1, isValid);
                    Mono<Void> sendUpdateBook = kafkaSender.send("loan-update-book-stock", updateBook);
                    return sendUpdateBook
                            .then(kafkaSender.send("loan-notification", loanNotificationMsg));
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
    ) {
    }

}
