package pe.ask.library.kafkalistener.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pe.ask.library.kafkalistener.helper.ReactiveKafkaListenerOperations;
import pe.ask.library.kafkalistener.payload.ValidateStock;
import pe.ask.library.kafkalistener.utils.ListenTopics;
import pe.ask.library.port.in.usecase.kafka.IValidateBookStockUseCase;
import reactor.core.publisher.Mono;

@Component
public class ValidateBookStockListener extends ReactiveKafkaListenerOperations<ValidateStock> {
    private final IValidateBookStockUseCase useCase;

    public ValidateBookStockListener(
            ObjectMapper mapper,
            IValidateBookStockUseCase useCase
    ) {
        super(mapper);
        this.useCase = useCase;
    }


    @Override
    public String getTargetTopic() {
        return ListenTopics.LOAN_VALIDATE_BOOK_STOCK;
    }

    @Override
    protected Class<ValidateStock> getPayloadClass() {
        return ValidateStock.class;
    }

    @Override
    protected Mono<Void> processRecord(ValidateStock payload) {
        return useCase.validateBookStock(payload.loanId(), payload.bookId());
    }
}
