package pe.ask.library.kafkalistener.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.kafkalistener.helper.ReactiveKafkaListenerOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReactiveKafkaListenerOperationsTest {

    @Mock
    private ObjectMapper mapper;

    private TestListenerOperations listenerOperations;

    static class TestDto {
        public String name;
    }

    @BeforeEach
    void setUp() {
        listenerOperations = new TestListenerOperations(mapper);
    }

    @Test
    @DisplayName("Should process message successfully when JSON is valid")
    void handleMessageSuccess() throws JsonProcessingException {
        String validJson = "{\"name\":\"test\"}";
        TestDto mockDto = new TestDto();
        mockDto.name = "test";

        when(mapper.readValue(validJson, TestDto.class)).thenReturn(mockDto);

        listenerOperations.setProcessResult(Mono.empty());

        StepVerifier.create(listenerOperations.handleMessage(validJson))
                .verifyComplete();

        verify(mapper).readValue(validJson, TestDto.class);
    }

    @Test
    @DisplayName("Should propagate error when deserialization fails")
    void handleMessageDeserializationError() throws JsonProcessingException {
        String invalidJson = "invalid-json";

        when(mapper.readValue(invalidJson, TestDto.class))
                .thenThrow(new JsonProcessingException("Error parsing") {});

        StepVerifier.create(listenerOperations.handleMessage(invalidJson))
                .expectError(JsonProcessingException.class)
                .verify();
    }

    @Test
    @DisplayName("Should log and propagate error when processRecord fails")
    void handleMessageProcessingError() throws JsonProcessingException {
        String validJson = "{\"name\":\"test\"}";
        TestDto mockDto = new TestDto();

        when(mapper.readValue(validJson, TestDto.class)).thenReturn(mockDto);

        listenerOperations.setProcessResult(Mono.error(new RuntimeException("Business Error")));

        StepVerifier.create(listenerOperations.handleMessage(validJson))
                .expectErrorMessage("Business Error")
                .verify();

        verify(mapper).readValue(validJson, TestDto.class);
    }

    static class TestListenerOperations extends ReactiveKafkaListenerOperations<TestDto> {

        private Mono<Void> processResult = Mono.empty();

        public TestListenerOperations(ObjectMapper mapper) {
            super(mapper);
        }

        public void setProcessResult(Mono<Void> result) {
            this.processResult = result;
        }

        @Override
        public String getTargetTopic() {
            return "test-topic";
        }

        @Override
        protected Class<TestDto> getPayloadClass() {
            return TestDto.class;
        }

        @Override
        protected Mono<Void> processRecord(TestDto payload) {
            return processResult;
        }
    }
}