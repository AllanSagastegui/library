package pe.ask.library.port.in.usecase.kafka;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IUpdateBookStockUseCase {
    Mono<Void> updateBookStock(UUID bookId, int quantity, boolean isIncrement);
}
