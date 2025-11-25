package pe.ask.library.kafkalistener.dispatcher;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.stereotype.Component;
import pe.ask.library.kafkalistener.helper.ReactiveKafkaListenerOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KafkaGlobalDispatcher {

    private final ReactiveKafkaConsumerTemplate<String, String> consumerTemplate;
    private final Map<String, ReactiveKafkaListenerOperations<?>> handlersMap;

    public KafkaGlobalDispatcher(
            ReactiveKafkaConsumerTemplate<String, String> consumerTemplate,
            List<ReactiveKafkaListenerOperations<?>> handlers
    ) {
        this.consumerTemplate = consumerTemplate;
        this.handlersMap = handlers.stream()
                .collect(Collectors.toMap(ReactiveKafkaListenerOperations::getTargetTopic, Function.identity()));
    }

    @EventListener(ApplicationStartedEvent.class)
    public Flux<Void> listenMessages() {
        return consumerTemplate
                .receiveAutoAck()
                .publishOn(Schedulers.boundedElastic())
                .flatMap(record -> {
                    String topic = record.topic();
                    String value = record.value();
                    ReactiveKafkaListenerOperations<?> handler = handlersMap.get(topic);
                    if (handler != null) {
                        return handler.handleMessage(value);
                    } else {
                        return Mono.empty();
                    }
                })
                .doOnError(error -> log.error("💥 Error crítico en el Dispatcher de Kafka", error))
                .retry()
                .repeat();
    }
}