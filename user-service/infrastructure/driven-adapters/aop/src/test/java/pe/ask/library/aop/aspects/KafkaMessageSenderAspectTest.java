package pe.ask.library.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import pe.ask.library.aop.dto.AuditLog;
import pe.ask.library.model.utils.IAuditable;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import pe.ask.library.port.out.kafka.KafkaSender;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMessageSenderAspectTest {

    @Mock
    private IKafkaMessageSenderPort kafkaMessageSenderPort;

    @Mock
    private ProceedingJoinPoint pjp;

    @Mock
    private KafkaSender kafkaSender;

    @Mock
    private Signature signature;

    private KafkaMessageSenderAspect aspect;

    @BeforeEach
    void setUp() {
        aspect = new KafkaMessageSenderAspect(kafkaMessageSenderPort);
        ReflectionTestUtils.setField(aspect, "msName", "test-service");
    }

    @Test
    void sendAuditLogSuccessWithAuditable() throws Throwable {
        String topic = "test-topic";
        String userId = "user-123";
        TestAuditable payload = new TestAuditable(userId);

        when(kafkaSender.topic()).thenReturn(topic);
        when(pjp.proceed()).thenReturn(Mono.just(payload));
        when(pjp.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("methodName");
        when(kafkaMessageSenderPort.send(eq(topic), any(AuditLog.class))).thenReturn(Mono.empty());

        Mono<Object> result = (Mono<Object>) aspect.sendAuditLog(pjp, kafkaSender);

        StepVerifier.create(result)
                .expectNext(payload)
                .verifyComplete();

        verify(kafkaMessageSenderPort).send(eq(topic), argThat(arg -> {
            AuditLog log = (AuditLog) arg;
            return log.userId().equals(userId) &&
                    log.msName().equals("test-service") &&
                    log.eventName().equals("AUDIT_LOG_SUCCESS");
        }));
    }

    @Test
    void sendAuditLogSuccessWithoutAuditable() throws Throwable {
        String topic = "test-topic";
        String payload = "simple-string";

        when(kafkaSender.topic()).thenReturn(topic);
        when(pjp.proceed()).thenReturn(Mono.just(payload));
        when(pjp.getSignature()).thenReturn(signature);
        when(signature.getName()).thenReturn("methodName");
        when(kafkaMessageSenderPort.send(eq(topic), any(AuditLog.class))).thenReturn(Mono.empty());

        Mono<Object> result = (Mono<Object>) aspect.sendAuditLog(pjp, kafkaSender);

        StepVerifier.create(result)
                .expectNext(payload)
                .verifyComplete();

        verify(kafkaMessageSenderPort).send(eq(topic), argThat(arg -> {
            AuditLog log = (AuditLog) arg;
            return log.userId().equals("UNKNOWN");
        }));
    }

    @Test
    void sendAuditLogNotMono() throws Throwable {
        String topic = "test-topic";
        String payload = "imperative-result";

        when(kafkaSender.topic()).thenReturn(topic);
        when(pjp.proceed()).thenReturn(payload);

        Object result = aspect.sendAuditLog(pjp, kafkaSender);

        assertEquals(payload, result);
        verify(kafkaMessageSenderPort, never()).send(any(), any());
    }

    static class TestAuditable implements IAuditable {
        private final String userId;

        TestAuditable(String userId) {
            this.userId = userId;
        }

        @Override
        public String getAuditUserId() {
            return userId;
        }
    }
}