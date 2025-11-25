package pe.ask.library.kafkasender.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import pe.ask.library.port.out.logger.Logger;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageSenderAdapter implements IKafkaMessageSenderPort {

    private final KafkaSender<String, String> reactorKafkaSender;
    private final ObjectMapper objectMapper;

    @Logger
    @Override
    public <T> Mono<Void> send(String topic, T message) {
        if (message == null) {
            log.warn("Message is null, skipping sending to topic: {}", topic);
            return Mono.empty();
        }
        return Mono.fromCallable(() -> serializeMessage(message))
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(jsonMessage -> {
                    log.debug("Serialized message for topic {}: {}", topic, jsonMessage);

                    var producerRecord = new ProducerRecord<String, String>(topic, null, jsonMessage);
                    var senderRecord = SenderRecord.create(producerRecord, null);

                    return reactorKafkaSender
                            .send(Mono.just(senderRecord))
                            .next();
                })
                .doOnError(e -> log.error("Error sending message to Kafka topic {}: {}", topic, e.getMessage()))
                .then();
    }

    private <T> String serializeMessage(T message) throws JsonProcessingException {
        if (message instanceof String str) {
            return str;
        }
        return objectMapper.writeValueAsString(message);
    }
}