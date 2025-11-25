package pe.ask.library.kafkasender.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.kafka.sender.SenderOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KafkaSenderConfigTest {

    @Mock
    private KafkaSenderProperties kafkaSenderProperties;

    @InjectMocks
    private KafkaSenderConfig kafkaSenderConfig;

    @BeforeEach
    void setUp() {
        KafkaSenderProperties.Producer producerProps = new KafkaSenderProperties.Producer();
        producerProps.setBootstrapServers("localhost:9092");
        producerProps.setKeySerializer("org.apache.kafka.common.serialization.StringSerializer");
        producerProps.setValueSerializer("org.apache.kafka.common.serialization.StringSerializer");

        when(kafkaSenderProperties.getProducer()).thenReturn(producerProps);
    }

    @Test
    @DisplayName("Should create SenderOptions bean with correct producer configuration")
    void senderOptionsSuccess() {
        SenderOptions<String, String> options = kafkaSenderConfig.senderOptions();

        assertNotNull(options);
        assertEquals("localhost:9092", options.producerProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG));
        assertEquals("org.apache.kafka.common.serialization.StringSerializer", options.producerProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG));
        assertEquals("org.apache.kafka.common.serialization.StringSerializer", options.producerProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG));
    }

    @Test
    @DisplayName("Should create KafkaSender bean successfully")
    void kafkaSenderSuccess() {
        SenderOptions<String, String> options = kafkaSenderConfig.senderOptions();

        assertNotNull(kafkaSenderConfig.kafkaSender(options));
    }
}