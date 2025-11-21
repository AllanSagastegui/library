package pe.ask.library.kafkasender.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "adapters.kafka")
public class KafkaProperties {

    private Producer producer = new Producer();
    @Data
    public static class Producer {
        private String bootstrapServers;
        private String keySerializer;
        private String valueSerializer;
    }
}
