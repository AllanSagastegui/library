package pe.ask.library.kafkalistener.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(KafkaListenerProperties.class)
public class KafkaListenerConfig {

    private final KafkaListenerProperties kafkaListenerProperties;

    @Bean
    public ReceiverOptions<String, String> receiverOptions() {
        var consumerProps = kafkaListenerProperties.getConsumer();

        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, consumerProps.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, consumerProps.getGroupId());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, consumerProps.getAutoOffsetReset());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, consumerProps.getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, consumerProps.getValueDeserializer());

        props.putAll(kafkaListenerProperties.getProperties());
        return ReceiverOptions.<String, String>create(props)
                .subscription(kafkaListenerProperties.getTopics());
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, String> kafkaReceiver(ReceiverOptions<String, String> kafkaReceiverOptions) {
        return new ReactiveKafkaConsumerTemplate<>(kafkaReceiverOptions);
    }
}