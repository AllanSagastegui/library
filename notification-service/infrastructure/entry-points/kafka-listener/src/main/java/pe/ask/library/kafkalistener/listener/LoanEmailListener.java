package pe.ask.library.kafkalistener.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import pe.ask.library.kafkalistener.helper.ReactiveKafkaListenerOperations;
import pe.ask.library.kafkalistener.payload.UserInfo;
import pe.ask.library.port.in.notification.ISendLoanEmail;
import reactor.core.publisher.Mono;

@Component
public class LoanEmailListener extends ReactiveKafkaListenerOperations<UserInfo> {

    private final ISendLoanEmail sendLoanEmail;

    public LoanEmailListener(
            ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumer,
            ObjectMapper mapper,
            ISendLoanEmail sendLoanEmail
    ) {
        super(reactiveKafkaConsumer, mapper);
        this.sendLoanEmail = sendLoanEmail;
    }

    @Override
    protected String getTargetTopic() {
        return "loan-email";
    }

    @Override
    protected Class<UserInfo> getPayloadClass() {
        return UserInfo.class;
    }

    @Override
    protected Mono<Void> processRecord(UserInfo payload) {
        return sendLoanEmail.sendLoanEmail(
                payload.loanId(),
                payload.email(),
                payload.completeName(),
                payload.loanDate(),
                payload.estimatedReturnDate(),
                payload.status()
        );
    }
}
