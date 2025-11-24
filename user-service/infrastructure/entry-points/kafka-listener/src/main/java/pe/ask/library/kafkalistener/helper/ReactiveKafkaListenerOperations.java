package pe.ask.library.kafkalistener.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
public abstract class ReactiveKafkaListenerOperations<T> {

    private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumer;
    private final ObjectMapper mapper;

    protected abstract String getTargetTopic();
    protected abstract Class<T> getPayloadClass();
    protected abstract Mono<Void> processRecord(T payload);

    @EventListener(ApplicationStartedEvent.class)
    public Flux<Void> listenMessage() {
        return reactiveKafkaConsumer
                .receiveAutoAck()
                .filter(payload -> getTargetTopic().equals(payload.topic()))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(payload ->
                        Mono.fromCallable(() -> mapper.readValue(payload.value(), getPayloadClass()))
                                .subscribeOn(Schedulers.boundedElastic())
                                .flatMap(this::processRecord)
                )
                .doOnError(error -> log.error("Error processing kafka record from topic {}", getTargetTopic(), error))
                .retry()
                .repeat();
    }
}
