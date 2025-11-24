package pe.ask.library.usecase.kafka;

import pe.ask.library.port.in.usecase.IProcessLoanNotification;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.utils.Status;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@UseCase
public class ProcessLoanNotification implements IProcessLoanNotification {

    private final IKafkaMessageSenderPort kafkaSender;

    public ProcessLoanNotification(IKafkaMessageSenderPort kafkaSender) {
        this.kafkaSender = kafkaSender;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Void> processLoanNotification(UUID loanId, UUID userId, LocalDateTime loanDate, LocalDateTime estimatedReturnDate, Status status) {
        return kafkaSender.send("notification-user-info", new GetUserInfo(loanId, userId, loanDate, estimatedReturnDate, status));
    }

    private record GetUserInfo(
            UUID loanId,
            UUID userId,
            LocalDateTime loanDate,
            LocalDateTime estimatedReturnDate,
            Status status
    ){ }
}
