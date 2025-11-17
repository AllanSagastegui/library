package pe.ask.library.kafka.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.stereotype.Component;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageSenderAdapter<T> implements IKafkaMessageSenderPort<T> {

    private final KafkaSender<String, String> reactorKafkaSender;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> send(String topic, T message) {
        return Mono.fromCallable(() -> serializeMessage(message))
                .flatMap(jsonMessage -> {
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
        if (message instanceof String) {
            return (String) message;
        }

        return objectMapper.writeValueAsString(message);
    }
}