package pe.ask.library.kafkasender.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMessageSenderAdapterTest {

    @Mock
    private KafkaSender<String, String> reactorKafkaSender;

    @Mock
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<Publisher<SenderRecord<String, String, Object>>> captor;

    private KafkaMessageSenderAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new KafkaMessageSenderAdapter(reactorKafkaSender, objectMapper);
    }

    @Test
    @DisplayName("Should serialize object to JSON and send message to Kafka successfully")
    void sendObjectSuccess() throws JsonProcessingException {
        String topic = "test-topic";
        TestDto message = new TestDto("value");
        String jsonMessage = "{\"field\":\"value\"}";

        when(objectMapper.writeValueAsString(message)).thenReturn(jsonMessage);
        when(reactorKafkaSender.send(any())).thenReturn(Flux.empty());

        StepVerifier.create(adapter.send(topic, message))
                .verifyComplete();

        verify(objectMapper).writeValueAsString(message);
        verify(reactorKafkaSender).send(captor.capture());

        StepVerifier.create(captor.getValue())
                .assertNext(record -> {
                    assertEquals(topic, record.topic());
                    assertEquals(jsonMessage, record.value());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should send String message directly skipping serialization")
    void sendStringSuccess() throws JsonProcessingException {
        String topic = "test-topic";
        String message = "raw-message";

        when(reactorKafkaSender.send(any())).thenReturn(Flux.empty());

        StepVerifier.create(adapter.send(topic, message))
                .verifyComplete();

        verify(objectMapper, never()).writeValueAsString(any());

        verify(reactorKafkaSender).send(captor.capture());
        StepVerifier.create(captor.getValue())
                .assertNext(record -> {
                    assertEquals(topic, record.topic());
                    assertEquals(message, record.value());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should ignore null messages and complete empty")
    void sendNullMessage() throws JsonProcessingException {
        StepVerifier.create(adapter.send("topic", null))
                .verifyComplete();

        verify(objectMapper, never()).writeValueAsString(any());
        verify(reactorKafkaSender, never()).send(any());
    }

    @Test
    @DisplayName("Should propagate error when serialization fails")
    void sendSerializationError() throws JsonProcessingException {
        Object message = new Object();
        when(objectMapper.writeValueAsString(message))
                .thenThrow(new JsonProcessingException("Error serializing") {});

        StepVerifier.create(adapter.send("topic", message))
                .expectError(JsonProcessingException.class)
                .verify();

        verify(reactorKafkaSender, never()).send(any());
    }

    @Test
    @DisplayName("Should propagate error when Kafka sender fails")
    void sendKafkaError() {
        String message = "data";
        when(reactorKafkaSender.send(any()))
                .thenReturn(Flux.error(new RuntimeException("Kafka error")));

        StepVerifier.create(adapter.send("topic", message))
                .expectErrorMessage("Kafka error")
                .verify();
    }

    static class TestDto {
        public String field;
        public TestDto(String field) { this.field = field; }
    }
}