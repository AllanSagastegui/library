package pe.ask.library.kafkalistener.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import pe.ask.library.kafkalistener.helper.ReactiveKafkaListenerOperations;
import pe.ask.library.kafkalistener.payload.GetUserInfo;
import pe.ask.library.port.in.usecase.user.IProcessGetUserInfoUseCase;
import reactor.core.publisher.Mono;

@Component
public class NotificationUserInfoListener extends ReactiveKafkaListenerOperations<GetUserInfo> {

    private final IProcessGetUserInfoUseCase useCase;

    public NotificationUserInfoListener(
            ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumer,
            ObjectMapper mapper,
            IProcessGetUserInfoUseCase useCase
    ) {
        super(reactiveKafkaConsumer, mapper);
        this.useCase = useCase;
    }

    @Override
    protected String getTargetTopic() {
        return "notification-user-info";
    }

    @Override
    protected Class<GetUserInfo> getPayloadClass() {
        return GetUserInfo.class;
    }

    @Override
    protected Mono<Void> processRecord(GetUserInfo payload) {
        return useCase.processGetUserInfo(
                payload.loanId(),
                payload.userId().toString(),
                payload.loanDate(),
                payload.estimatedReturnDate(),
                payload.status()
        );
    }
}
