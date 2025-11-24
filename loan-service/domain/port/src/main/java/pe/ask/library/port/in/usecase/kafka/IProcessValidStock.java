package pe.ask.library.port.in.usecase.kafka;

import reactor.core.publisher.Mono;

import java.util.UUID;

@FunctionalInterface
public interface IProcessValidStock {
    Mono<Void> processValidStock(UUID loanId, UUID bookId, Boolean isValid);
}
