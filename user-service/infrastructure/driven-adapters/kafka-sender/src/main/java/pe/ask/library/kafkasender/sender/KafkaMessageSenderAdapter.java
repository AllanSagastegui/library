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
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageSenderAdapter<T> implements IKafkaMessageSenderPort<T> {

    private final KafkaSender<String, String> reactorKafkaSender;
    private final ObjectMapper objectMapper;

    @Logger
    @Override
    public Mono<Void> send(String topic, T message) {
        log.info("Sending message to topic: {}", topic);
        return Mono.fromCallable(() -> serializeMessage(message))
                .flatMap(jsonMessage -> {
                    log.info("Serialized message: {}", jsonMessage);
                    var producerRecord = new ProducerRecord<String, String>(topic, null, jsonMessage);

                    var senderRecord = SenderRecord.create(
                            producerRecord,
                            null
                    );

                    return reactorKafkaSender
                            .send(Mono.just(senderRecord))
                            .next();
                })
                .then();
    }

    private String serializeMessage(T message) throws JsonProcessingException {
        log.info("Serializing message: {}", message);
        if (message instanceof String) {
            return (String) message;
        }

        return objectMapper.writeValueAsString(message);
    }
}