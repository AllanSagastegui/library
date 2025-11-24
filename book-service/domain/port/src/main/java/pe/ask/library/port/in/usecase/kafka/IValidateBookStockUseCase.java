package pe.ask.library.port.in.usecase.kafka;

import reactor.core.publisher.Mono;

import java.util.UUID;

@FunctionalInterface
public interface IValidateBookStockUseCase {
    Mono<Void> validateBookStock(UUID loanId, UUID bookId);
}
