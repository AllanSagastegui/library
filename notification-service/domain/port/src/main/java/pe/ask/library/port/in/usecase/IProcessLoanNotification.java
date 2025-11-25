package pe.ask.library.port.in.usecase;

import pe.ask.library.port.utils.Status;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@FunctionalInterface
public interface IProcessLoanNotification {
    Mono<Void> processLoanNotification(UUID loanId,
                                       String userId,
                                       LocalDateTime loanDate,
                                       LocalDateTime estimatedReturnDate,
                                       Status status);
}
