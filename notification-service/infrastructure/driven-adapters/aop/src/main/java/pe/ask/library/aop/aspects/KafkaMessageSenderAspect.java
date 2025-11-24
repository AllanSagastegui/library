package pe.ask.library.aop.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.ask.library.aop.dto.AuditLog;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import pe.ask.library.port.out.kafka.KafkaSender;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class KafkaMessageSenderAspect {

    private final IKafkaMessageSenderPort kafkaMessageSenderPort;

    @Value("${spring.application.name:user-service}")
    private String msName;

    private static final String SERVICE_USER = "notification-send";

    @Around("@annotation(kafkaSender)")
    public Object sendAuditLog(ProceedingJoinPoint pjp, KafkaSender kafkaSender) throws Throwable {

        Object result = pjp.proceed();
        String topic = kafkaSender.topic();
        String methodName = pjp.getSignature().getName();

        if (result instanceof Mono<?> monoResult) {
            return monoResult
                    .flatMap(payload -> {
                        AuditLog logSuccess = buildLog(methodName, "AUDIT_LOG_SUCCESS");
                        return kafkaMessageSenderPort.send(topic, logSuccess)
                                .thenReturn(payload);
                    })
                    .onErrorResume(ex -> {
                        log.error("Error detectado en método {}: {}", methodName, ex.getMessage());
                        AuditLog logFailed = buildLog(methodName, "AUDIT_LOG_FAILED");
                        return kafkaMessageSenderPort.send(topic, logFailed)
                                .then(Mono.error(ex));
                    });
        }
        return result;
    }

    private AuditLog buildLog(String methodName, String status) {
        return new AuditLog(
                UUID.randomUUID(),
                msName,
                methodName,
                status,
                KafkaMessageSenderAspect.SERVICE_USER,
                LocalDateTime.now()
        );
    }
}