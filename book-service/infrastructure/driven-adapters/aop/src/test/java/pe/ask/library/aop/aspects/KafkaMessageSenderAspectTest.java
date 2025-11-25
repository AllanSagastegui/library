package pe.ask.library.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaMessageSenderAspectTest {

    @Mock
    private IKafkaMessageSenderPort kafkaMessageSenderPort;

    @Mock
    private IAuditable auditable;

    @Mock
    private ProceedingJoinPoint pjp;

    @Mock
    private KafkaSender kafkaSender;

    @Mock
    private Signature signature;

    private KafkaMessageSenderAspect aspect;

    @BeforeEach
    void setUp() {
        aspect = new KafkaMessageSenderAspect(kafkaMessageSenderPort, auditable);
        ReflectionTestUtils.setField(aspect, "msName", "test-service");

        lenient().when(pjp.getSignature()).thenReturn(signature);
        lenient().when(signature.getName()).thenReturn("methodName");
        lenient().when(kafkaSender.topic()).thenReturn("test-topic");
    }

    @Test
    @DisplayName("Should send SUCCESS log with userId when auditable returns a user")
    void sendAuditLogSuccessWithUser() throws Throwable {
        String userId = "user-123";
        String payload = "domain-response";

        when(auditable.getAuditUserId()).thenReturn(Mono.just(userId));
        when(pjp.proceed()).thenReturn(Mono.just(payload));
        when(kafkaMessageSenderPort.send(any(), any(AuditLog.class))).thenReturn(Mono.empty());

        Mono<Object> result = (Mono<Object>) aspect.sendAuditLog(pjp, kafkaSender);

        StepVerifier.create(result)
                .expectNext(payload)
                .verifyComplete();

        verify(kafkaMessageSenderPort).send(eq("test-topic"), argThat(arg -> {
            AuditLog log = (AuditLog) arg;
            return log.userId().equals(userId) &&
                    log.eventName().equals("AUDIT_LOG_SUCCESS");
        }));
    }

    @Test
    @DisplayName("Should send SUCCESS log with 'anonymous' when auditable is empty")
    void sendAuditLogSuccessAnonymous() throws Throwable {
        String payload = "domain-response";

        when(auditable.getAuditUserId()).thenReturn(Mono.empty());
        when(pjp.proceed()).thenReturn(Mono.just(payload));
        when(kafkaMessageSenderPort.send(any(), any(AuditLog.class))).thenReturn(Mono.empty());

        StepVerifier.create((Mono<Object>) aspect.sendAuditLog(pjp, kafkaSender))
                .expectNext(payload)
                .verifyComplete();

        verify(kafkaMessageSenderPort).send(eq("test-topic"), argThat(arg -> {
            AuditLog log = (AuditLog) arg;
            return log.userId().equals("anonymous");
        }));
    }

    @Test
    @DisplayName("Should send FAILED log and propagate error when method throws exception")
    void sendAuditLogFailure() throws Throwable {
        String userId = "user-error";
        RuntimeException error = new RuntimeException("Business Error");

        when(auditable.getAuditUserId()).thenReturn(Mono.just(userId));
        when(pjp.proceed()).thenReturn(Mono.error(error));
        when(kafkaMessageSenderPort.send(any(), any(AuditLog.class))).thenReturn(Mono.empty());

        StepVerifier.create((Mono<Object>) aspect.sendAuditLog(pjp, kafkaSender))
                .expectError(RuntimeException.class)
                .verify();
        verify(kafkaMessageSenderPort).send(eq("test-topic"), argThat(arg -> {
            AuditLog log = (AuditLog) arg;
            return log.userId().equals(userId) &&
                    log.eventName().equals("AUDIT_LOG_FAILED");
        }));
    }

    @Test
    @DisplayName("Should ignore auditing if return type is not Mono (Imperative)")
    void sendAuditLogImperative() throws Throwable {
        String payload = "imperative-result";
        when(pjp.proceed()).thenReturn(payload);

        Object result = aspect.sendAuditLog(pjp, kafkaSender);

        assertEquals(payload, result);
        verifyNoInteractions(auditable);
        verifyNoInteractions(kafkaMessageSenderPort);
    }
}