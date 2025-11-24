package pe.ask.library.port.in.notification;

import pe.ask.library.port.utils.Status;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@FunctionalInterface
public interface ISendLoanEmail {
    Mono<Void> sendLoanEmail(
            UUID loanId,
            String email,
            String completeName,
            LocalDateTime loanDate,
            LocalDateTime estimatedReturnDate,
            Status status
    );
}
