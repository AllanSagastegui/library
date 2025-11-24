package pe.ask.library.port.out.kafka;

import reactor.core.publisher.Mono;

@FunctionalInterface
public interface IKafkaMessageSenderPort {
    <T> Mono<Void> send(String topic, T message);
}
