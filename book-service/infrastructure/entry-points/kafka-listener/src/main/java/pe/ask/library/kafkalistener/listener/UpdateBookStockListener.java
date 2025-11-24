package pe.ask.library.kafkalistener.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import pe.ask.library.kafkalistener.helper.ReactiveKafkaListenerOperations;
import pe.ask.library.kafkalistener.payload.UpdateBook;
import pe.ask.library.kafkalistener.utils.ListenTopics;
import pe.ask.library.port.in.usecase.kafka.IUpdateBookStockUseCase;
import reactor.core.publisher.Mono;

@Component
public class UpdateBookStockListener extends ReactiveKafkaListenerOperations<UpdateBook> {

    private final IUpdateBookStockUseCase useCase;

    public UpdateBookStockListener(
            ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumer,
            ObjectMapper mapper,
            IUpdateBookStockUseCase useCase
    ) {
        super(reactiveKafkaConsumer, mapper);
        this.useCase = useCase;
    }

    @Override
    protected String getTargetTopic() {
        return ListenTopics.LOAN_UPDATE_BOOK_STOCK;
    }

    @Override
    protected Class<UpdateBook> getPayloadClass() {
        return UpdateBook.class;
    }

    @Override
    protected Mono<Void> processRecord(UpdateBook payload) {
        return useCase.updateBookStock(payload.bookId(), payload.quantity(), payload.isIncrement());
    }
}
