package pe.ask.library.kafkalistener.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ConfigurationProperties(prefix = "entry-points.kafka")
public class KafkaProperties {

    private String auditTopic;
    private Consumer consumer = new Consumer();

    private Map<String, Object> properties = new HashMap<>();

    @Data
    public static class Consumer {
        private String bootstrapServers;
        private String groupId;
        private String autoOffsetReset;
        private String keyDeserializer;
        private String valueDeserializer;
    }
}
