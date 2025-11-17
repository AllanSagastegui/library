package pe.ask.library.aop.aspects;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pe.ask.library.aop.dto.AuditLog;
import pe.ask.library.model.utils.IAuditable;
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

    private final IKafkaMessageSenderPort<AuditLog> kafkaMessageSenderPort;

    @Value("${spring.application.name:user-service}")
    private String msName;

    @Around("@annotation(kafkaSender)")
    public Object sendAuditLog(ProceedingJoinPoint pjp, KafkaSender kafkaSender) throws Throwable {

        Object result = pjp.proceed();
        String topic = kafkaSender.topic();

        if (result instanceof Mono<?> monoResult) {
            return monoResult.flatMap(payload -> {
                String userId = "UNKNOWN";
                if (payload instanceof IAuditable auditable) {
                    userId = auditable.getAuditUserId();
                }

                AuditLog logData;
                logData = new AuditLog(
                        UUID.randomUUID(),
                        msName,
                        pjp.getSignature().getName(),
                        "AUDIT_LOG_SUCCESS",
                        userId,
                        LocalDateTime.now()
                );
                return kafkaMessageSenderPort.send(topic, logData)
                        .thenReturn(payload);
            });
        }

        return result;
    }
}
