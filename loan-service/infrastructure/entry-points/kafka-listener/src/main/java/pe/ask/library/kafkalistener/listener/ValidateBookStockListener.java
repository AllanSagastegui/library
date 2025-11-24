package pe.ask.library.kafkalistener.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import pe.ask.library.kafkalistener.helper.ReactiveKafkaListenerOperations;
import pe.ask.library.kafkalistener.payload.ValidStock;
import pe.ask.library.kafkalistener.utils.ListenTopics;
import pe.ask.library.port.in.usecase.kafka.IProcessValidStock;
import reactor.core.publisher.Mono;

@Component
public class ValidateBookStockListener extends ReactiveKafkaListenerOperations<ValidStock> {

    private final IProcessValidStock useCase;

    public ValidateBookStockListener(
            ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumer,
            ObjectMapper mapper,
            IProcessValidStock useCase
    ) {
        super(reactiveKafkaConsumer, mapper);
        this.useCase = useCase;
    }

    @Override
    protected String getTargetTopic() {
        return ListenTopics.BOOK_VALIDATE_STOCK;
    }

    @Override
    protected Class<ValidStock> getPayloadClass() {
        return ValidStock.class;
    }

    @Override
    protected Mono<Void> processRecord(ValidStock payload) {
        return useCase.processValidStock(payload.loanId(), payload.bookId(), payload.isValid());
    }
}
