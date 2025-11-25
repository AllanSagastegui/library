package pe.ask.library.kafkalistener.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pe.ask.library.kafkalistener.helper.ReactiveKafkaListenerOperations;
import pe.ask.library.kafkalistener.payload.UserInfo;
import pe.ask.library.port.in.notification.ISendLoanEmail;
import reactor.core.publisher.Mono;

@Component
public class LoanEmailListener extends ReactiveKafkaListenerOperations<UserInfo> {

    private final ISendLoanEmail sendLoanEmail;

    public LoanEmailListener(
            ObjectMapper mapper,
            ISendLoanEmail sendLoanEmail
    ) {
        super(mapper);
        this.sendLoanEmail = sendLoanEmail;
    }

    @Override
    public String getTargetTopic() {
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
