package pe.ask.library.usecase.kafka;

import pe.ask.library.port.in.usecase.kafka.IValidateBookStockUseCase;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IBookRepository;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@UseCase
public class ValidateBookStockUseCase implements IValidateBookStockUseCase {

    private final IBookRepository repository;
    private final IKafkaMessageSenderPort kafkaSender;

    public ValidateBookStockUseCase(IBookRepository repository, IKafkaMessageSenderPort kafkaSender) {
        this.repository = repository;
        this.kafkaSender = kafkaSender;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Void> validateBookStock(UUID loanId, UUID bookId) {
        return repository.validateBookStock(bookId)
                .map(isValid -> new ValidStock(loanId, bookId, isValid))
                .publishOn(Schedulers.boundedElastic())
                .doOnNext(response -> kafkaSender.send("book-validate-stock", response)
                        .subscribe()
                )
                .then();
    }

    public record ValidStock(
            UUID loanId,
            UUID bookId,
            Boolean isValid
    ){}
}
