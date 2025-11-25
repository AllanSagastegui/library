package pe.ask.library.kafkasender.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class KafkaSenderPropertiesTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(TestConfig.class);

    @Test
    @DisplayName("Should bind Kafka sender properties correctly from configuration")
    void testPropertiesBinding() {
        contextRunner
                .withPropertyValues(
                        "adapters.kafka.producer.bootstrap-servers=localhost:9092",
                        "adapters.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
                        "adapters.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer"
                )
                .run(context -> {
                    KafkaSenderProperties props = context.getBean(KafkaSenderProperties.class);

                    assertThat(props.getProducer()).isNotNull();
                    assertThat(props.getProducer().getBootstrapServers()).isEqualTo("localhost:9092");
                    assertThat(props.getProducer().getKeySerializer()).isEqualTo("org.apache.kafka.common.serialization.StringSerializer");
                    assertThat(props.getProducer().getValueSerializer()).isEqualTo("org.apache.kafka.common.serialization.StringSerializer");
                });
    }

    @EnableConfigurationProperties(KafkaSenderProperties.class)
    static class TestConfig {
    }
}