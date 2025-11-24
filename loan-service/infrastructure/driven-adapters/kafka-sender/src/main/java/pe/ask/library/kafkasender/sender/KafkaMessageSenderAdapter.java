package pe.ask.library.kafkasender.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers; // Importar Schedulers
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageSenderAdapter implements IKafkaMessageSenderPort {

    private final KafkaSender<String, String> reactorKafkaSender;
    private final ObjectMapper objectMapper;

    @Override
    public <T> Mono<Void> send(String topic, T message) {
        log.info("Preparing to send message to topic: {}", topic);
        return Mono.fromCallable(() -> serializeMessage(message))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(jsonMessage -> {
                    log.debug("Serialized message: {}", jsonMessage);
                    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, jsonMessage);
                    SenderRecord<String, String, Object> senderRecord = SenderRecord.create(producerRecord, null);
                    return reactorKafkaSender
                            .send(Mono.just(senderRecord))
                            .then();
                })
                .doOnError(error -> log.error("Error sending message to Kafka topic: {}", topic, error))
                .then();
    }

    private <T> String serializeMessage(T message) throws JsonProcessingException {
        if (message == null) {
            return null;
        }

        if (message instanceof String) {
            return (String) message;
        }

        return objectMapper.writeValueAsString(message);
    }
}