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
                // 1. Log cuando la aplicación arranca y se suscribe
                .doOnSubscribe(subscription -> log.info("⚡ [KAFKA-LISTENER] Iniciando suscripción al tópico: {}", getTargetTopic()))
                .filter(payload -> getTargetTopic().equals(payload.topic()))
                // 2. Log cuando llega el mensaje crudo (Raw JSON)
                .doOnNext(record -> log.info("📩 [KAFKA-RECEIVE] Mensaje recibido en {}: \nPayload: {}", record.topic(), record.value()))
                .publishOn(Schedulers.boundedElastic())
                .flatMap(record ->
                        Mono.fromCallable(() -> {
                                    // 3. Intento de deserialización
                                    log.debug("⚙️ [KAFKA-DESERIALIZE] Convirtiendo mensaje a {}", getPayloadClass().getSimpleName());
                                    return mapper.readValue(record.value(), getPayloadClass());
                                })
                                .subscribeOn(Schedulers.boundedElastic())
                                .flatMap(payload -> {
                                    // 4. Antes de ejecutar tu lógica de negocio
                                    log.info("🚀 [KAFKA-PROCESS] Ejecutando processRecord para: {}", payload);
                                    return processRecord(payload);
                                })
                                // 5. Log de éxito tras terminar processRecord
                                .doOnSuccess(unused -> log.info("✅ [KAFKA-SUCCESS] Mensaje procesado correctamente en {}", getTargetTopic()))
                )
                .doOnError(error -> log.error("❌ [KAFKA-ERROR] Error procesando registro Kafka en tópico {}", getTargetTopic(), error))
                .retry()
                .repeat()
                .doOnSubscribe(s -> log.info("🔌 [KAFKA-CONN] Flujo reactivo conectado."));
    }
}