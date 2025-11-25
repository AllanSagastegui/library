package pe.ask.library.kafkalistener.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Getter
@RequiredArgsConstructor
public abstract class ReactiveKafkaListenerOperations<T> {

    private final ObjectMapper mapper;

    public abstract String getTargetTopic();
    protected abstract Class<T> getPayloadClass();
    protected abstract Mono<Void> processRecord(T payload);

    public Mono<Void> handleMessage(String jsonMessage) {
        return Mono.fromCallable(() -> mapper.readValue(jsonMessage, getPayloadClass()))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(this::processRecord)
                .doOnError(e -> log.error("Error procesando mensaje del tópico {}", getTargetTopic(), e));
    }
}