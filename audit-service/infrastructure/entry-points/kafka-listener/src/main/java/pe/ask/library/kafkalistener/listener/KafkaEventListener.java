package pe.ask.library.kafkalistener.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import pe.ask.library.model.auditlog.AuditLog;
import pe.ask.library.port.in.usecase.auditlog.ICreateNewAuditLogUseCase;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventListener {

    private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumer;
    private final ObjectMapper mapper;
    private final ICreateNewAuditLogUseCase createNewAuditLogUseCase;

    @EventListener(ApplicationStartedEvent.class)
    public Flux<Void> listenMessages() {
        return reactiveKafkaConsumer
                .receiveAutoAck()
                .publishOn(Schedulers.boundedElastic())
                .flatMap(auditLog ->
                        Mono.fromCallable(() -> mapper.readValue(auditLog.value(), AuditLog.class))
                                .subscribeOn(Schedulers.boundedElastic())
                                .flatMap(createNewAuditLogUseCase::execute)
                )
                .doOnError(error -> log.error("Error processing kafka record", error))
                .retry()
                .repeat();
    }
}