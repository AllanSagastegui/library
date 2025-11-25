package pe.ask.library.kafkalistener.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import pe.ask.library.kafkalistener.helper.ReactiveKafkaListenerOperations;
import pe.ask.library.kafkalistener.payload.WelcomeEmail;
import pe.ask.library.port.in.notification.ISendWelcomeEmail;
import reactor.core.publisher.Mono;

@Component
public class WelcomeEmailListener extends ReactiveKafkaListenerOperations<WelcomeEmail> {

    private final ISendWelcomeEmail sendWelcomeEmail;

    public WelcomeEmailListener(
            ObjectMapper mapper,
            ISendWelcomeEmail sendWelcomeEmail
    ) {
        super(mapper);
        this.sendWelcomeEmail = sendWelcomeEmail;
    }

    @Override
    public String getTargetTopic() {
        return "welcome-email";
    }

    @Override
    protected Class<WelcomeEmail> getPayloadClass() {
        return WelcomeEmail.class;
    }

    @Override
    protected Mono<Void> processRecord(WelcomeEmail payload) {
        return sendWelcomeEmail.sendWelcomeEmail(payload.email(), payload.completeName());
    }
}
