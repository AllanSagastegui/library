package pe.ask.library.kafkalistener.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class KafkaListenerConfigTest {

    @InjectMocks
    private KafkaListenerConfig kafkaListenerConfig;

    @Mock
    private KafkaListenerProperties kafkaListenerProperties;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);

        KafkaListenerProperties.Consumer consumerProps = new KafkaListenerProperties.Consumer();
        consumerProps.setBootstrapServers("localhost:9092");
        consumerProps.setGroupId("test-group");
        consumerProps.setAutoOffsetReset("earliest");
        consumerProps.setKeyDeserializer("org.apache.kafka.common.serialization.StringDeserializer");
        consumerProps.setValueDeserializer("org.apache.kafka.common.serialization.StringDeserializer");

        when(kafkaListenerProperties.getConsumer()).thenReturn(consumerProps);
        when(kafkaListenerProperties.getTopics()).thenReturn(List.of("test-topic"));
        when(kafkaListenerProperties.getProperties()).thenReturn(new HashMap<>());
    }

    @AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    @DisplayName("Should create ReceiverOptions bean successfully")
    void receiverOptionsSuccess() {
        ReceiverOptions<String, String> options = kafkaListenerConfig.receiverOptions();
        assertNotNull(options);
    }

    @Test
    @DisplayName("Should create ReactiveKafkaConsumerTemplate bean successfully")
    void kafkaReceiverSuccess() {
        ReceiverOptions<String, String> options = kafkaListenerConfig.receiverOptions();
        
        assertNotNull(kafkaListenerConfig.kafkaReceiver(options));
    }
}