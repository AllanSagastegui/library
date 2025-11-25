package pe.ask.library.kafkalistener.dispatcher;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import pe.ask.library.kafkalistener.helper.ReactiveKafkaListenerOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaGlobalDispatcherTest {

    @Mock
    private ReactiveKafkaConsumerTemplate<String, String> consumerTemplate;

    @Mock
    private ReactiveKafkaListenerOperations<Object> handler;

    private KafkaGlobalDispatcher dispatcher;

    private final String TEST_TOPIC = "test-topic";
    private final String UNKNOWN_TOPIC = "unknown-topic";
    private final String MSG_VALUE = "message-content";

    @BeforeEach
    void setUp() {
        lenient().when(handler.getTargetTopic()).thenReturn(TEST_TOPIC);
        dispatcher = new KafkaGlobalDispatcher(consumerTemplate, List.of(handler));
    }

    @Test
    @DisplayName("Should dispatch message to correct handler when topic exists")
    void listenMessagesDispatchSuccess() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>(TEST_TOPIC, 0, 0L, "key", MSG_VALUE);

        when(consumerTemplate.receiveAutoAck())
                .thenReturn(Flux.concat(Flux.just(record), Flux.never()));

        when(handler.handleMessage(MSG_VALUE)).thenReturn(Mono.empty());

        StepVerifier.create(dispatcher.listenMessages())
                .expectSubscription()
                .expectNoEvent(Duration.ofMillis(200))
                .thenCancel()
                .verify();

        verify(handler, timeout(1000).times(1)).handleMessage(MSG_VALUE);
    }

    @Test
    @DisplayName("Should ignore message when no handler is found for topic")
    void listenMessagesTopicNotFound() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>(UNKNOWN_TOPIC, 0, 0L, "key", MSG_VALUE);

        when(consumerTemplate.receiveAutoAck())
                .thenReturn(Flux.concat(Flux.just(record), Flux.never()));

        StepVerifier.create(dispatcher.listenMessages())
                .expectSubscription()
                .expectNoEvent(Duration.ofMillis(200))
                .thenCancel()
                .verify();

        verify(handler, never()).handleMessage(anyString());
    }

    @Test
    @DisplayName("Should retry and continue listening when handler throws error")
    void listenMessagesErrorHandler() {
        ConsumerRecord<String, String> record = new ConsumerRecord<>(TEST_TOPIC, 0, 0L, "key", MSG_VALUE);

        when(consumerTemplate.receiveAutoAck())
                .thenReturn(Flux.concat(Flux.just(record), Flux.never()));

        when(handler.handleMessage(MSG_VALUE))
                .thenReturn(Mono.error(new RuntimeException("Processing failed")));

        StepVerifier.create(dispatcher.listenMessages())
                .expectSubscription()
                .expectNoEvent(Duration.ofMillis(200))
                .thenCancel()
                .verify();

        verify(handler, atLeast(1)).handleMessage(MSG_VALUE);
    }

    @Test
    @DisplayName("Should initialize correctly with empty handlers list")
    void constructorWithEmptyList() {
        KafkaGlobalDispatcher emptyDispatcher = new KafkaGlobalDispatcher(consumerTemplate, Collections.emptyList());

        ConsumerRecord<String, String> record = new ConsumerRecord<>(TEST_TOPIC, 0, 0L, "key", MSG_VALUE);
        when(consumerTemplate.receiveAutoAck())
                .thenReturn(Flux.concat(Flux.just(record), Flux.never()));

        StepVerifier.create(emptyDispatcher.listenMessages())
                .expectSubscription()
                .expectNoEvent(Duration.ofMillis(100))
                .thenCancel()
                .verify();
    }
}