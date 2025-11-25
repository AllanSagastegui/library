package pe.ask.library.kafkalistener.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pe.ask.library.kafkalistener.helper.ReactiveKafkaListenerOperations;
import pe.ask.library.kafkalistener.payload.LoanNotificationMsg;
import pe.ask.library.port.in.usecase.IProcessLoanNotification;
import reactor.core.publisher.Mono;

@Component
public class LoanNotificationListener extends ReactiveKafkaListenerOperations<LoanNotificationMsg> {

    private final IProcessLoanNotification processLoanNotification;

    public LoanNotificationListener(
            ObjectMapper mapper,
            IProcessLoanNotification processLoanNotification
    ) {
        super(mapper);
        this.processLoanNotification = processLoanNotification;
    }

    @Override
    public String getTargetTopic() {
        return "loan-notification";
    }

    @Override
    protected Class<LoanNotificationMsg> getPayloadClass() {
        return LoanNotificationMsg.class;
    }

    @Override
    protected Mono<Void> processRecord(LoanNotificationMsg payload) {
        return processLoanNotification.processLoanNotification(
                payload.loanId(),
                payload.userId(),
                payload.loanDate(),
                payload.estimatedReturnDate(),
                payload.status()
        );
    }
}
