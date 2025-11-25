package pe.ask.library.kafkalistener.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class KafkaListenerPropertiesTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class)
            .withPropertyValues(
                    "entry-points.kafka.topics[0]=topic-a",
                    "entry-points.kafka.topics[1]=topic-b",

                    "entry-points.kafka.consumer.bootstrap-servers=localhost:9092",
                    "entry-points.kafka.consumer.group-id=my-group-id",
                    "entry-points.kafka.consumer.auto-offset-reset=latest",
                    "entry-points.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer",
                    "entry-points.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer",

                    "entry-points.kafka.properties[security.protocol]=SASL_SSL"
            );

    @Test
    @DisplayName("Should bind Kafka listener properties correctly from configuration")
    void testPropertiesBinding() {
        contextRunner.run(context -> {
            KafkaListenerProperties props = context.getBean(KafkaListenerProperties.class);

            assertThat(props.getTopics()).containsExactly("topic-a", "topic-b");

            assertThat(props.getConsumer()).isNotNull();
            assertThat(props.getConsumer().getBootstrapServers()).isEqualTo("localhost:9092");
            assertThat(props.getConsumer().getGroupId()).isEqualTo("my-group-id");
            assertThat(props.getConsumer().getAutoOffsetReset()).isEqualTo("latest");
            assertThat(props.getConsumer().getKeyDeserializer()).isEqualTo("org.apache.kafka.common.serialization.StringDeserializer");
            assertThat(props.getConsumer().getValueDeserializer()).isEqualTo("org.apache.kafka.common.serialization.StringDeserializer");

            assertThat(props.getProperties()).containsEntry("security.protocol", "SASL_SSL");
        });
    }

    @EnableConfigurationProperties(KafkaListenerProperties.class)
    static class TestConfig {
    }
}