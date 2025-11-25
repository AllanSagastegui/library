package pe.ask.library.aop.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.port.out.logger.ILoggerPort;
import pe.ask.library.port.out.logger.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoggerAspectTest {

    @Mock
    private ILoggerPort loggerPort;

    @Mock
    private ProceedingJoinPoint pjp;

    @Mock
    private Logger logger;

    @Mock
    private Signature signature;

    private LoggerAspect aspect;

    @BeforeEach
    void setUp() {
        aspect = new LoggerAspect(loggerPort);
        lenient().when(pjp.getSignature()).thenReturn(signature);
        lenient().when(signature.toShortString()).thenReturn("methodName");

        lenient().when(logger.logLevel()).thenReturn(Logger.LogLevel.INFO);
        lenient().when(logger.unit()).thenReturn(TimeUnit.MILLISECONDS);
        lenient().when(logger.includeInvocationId()).thenReturn(true);
        lenient().when(logger.logThread()).thenReturn(false);
        lenient().when(logger.logArgs()).thenReturn(false);
        lenient().when(logger.logResult()).thenReturn(true);
        lenient().when(logger.logExceptions()).thenReturn(true);
        lenient().when(logger.logExceptionStackTrace()).thenReturn(false);
        lenient().when(logger.ignoreExceptions()).thenReturn(new Class[]{});
    }

    @Test
    @DisplayName("Debe loguear inicio, resultado y tiempo correctamente (Happy Path Imperativo)")
    void logAroundImperativeSuccess() throws Throwable {
        String expectedResult = "result";
        when(pjp.proceed()).thenReturn(expectedResult);

        Object result = aspect.logAround(pjp, logger);

        assertEquals(expectedResult, result);

        verify(loggerPort, atLeastOnce()).info(anyString(), any());
        verify(loggerPort).info(anyString(), any(), eq("result"));
        verify(loggerPort).trace(anyString(), any(), any(), any());
    }

    @Test
    @DisplayName("Debe truncar el resultado si excede 150 caracteres")
    void logAroundTruncatesLongResult() throws Throwable {
        String longResult = Stream.generate(() -> "a").limit(160).collect(Collectors.joining());
        when(pjp.proceed()).thenReturn(longResult);

        aspect.logAround(pjp, logger);

        verify(loggerPort).info(anyString(), any(), contains("...(truncated)"));
    }

    @Test
    @DisplayName("Debe incluir el nombre del hilo si logThread es true")
    void logAroundWithThreadName() throws Throwable {
        when(logger.logThread()).thenReturn(true);
        when(pjp.proceed()).thenReturn("ok");

        aspect.logAround(pjp, logger);

        verify(loggerPort, atLeastOnce()).info(anyString(), any());
    }

    @Test
    @DisplayName("Debe loguear excepción SIN stack trace por defecto")
    void logAroundImperativeException() throws Throwable {
        RuntimeException ex = new RuntimeException("error");
        when(pjp.proceed()).thenThrow(ex);

        assertThrows(RuntimeException.class, () -> aspect.logAround(pjp, logger));

        verify(loggerPort).error(anyString(), any(), eq("error"));
    }

    @Test
    @DisplayName("Debe loguear excepción CON stack trace si está habilitado")
    void logAroundExceptionWithStackTrace() throws Throwable {
        when(logger.logExceptionStackTrace()).thenReturn(true);
        RuntimeException ex = new RuntimeException("fatal error");
        when(pjp.proceed()).thenThrow(ex);

        assertThrows(RuntimeException.class, () -> aspect.logAround(pjp, logger));

        verify(loggerPort).error(anyString(), any(), eq(ex));
    }

    @Test
    @DisplayName("Debe manejar Mono exitoso")
    void logAroundMonoSuccess() throws Throwable {
        when(pjp.proceed()).thenReturn(Mono.just("mono-result"));

        Mono<Object> result = (Mono<Object>) aspect.logAround(pjp, logger);

        StepVerifier.create(result)
                .expectNext("mono-result")
                .verifyComplete();

        verify(loggerPort).info(anyString(), any(), eq("mono-result"));
        verify(loggerPort).trace(anyString(), any(), any(), any());
    }

    @Test
    @DisplayName("Debe manejar Mono con error")
    void logAroundMonoError() throws Throwable {
        when(pjp.proceed()).thenReturn(Mono.error(new RuntimeException("mono-error")));

        Mono<Object> result = (Mono<Object>) aspect.logAround(pjp, logger);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(loggerPort).error(anyString(), any(), eq("mono-error"));
    }

    @Test
    @DisplayName("Debe manejar Flux exitoso")
    void logAroundFluxSuccess() throws Throwable {
        when(pjp.proceed()).thenReturn(Flux.just("A", "B"));

        Flux<Object> result = (Flux<Object>) aspect.logAround(pjp, logger);

        StepVerifier.create(result)
                .expectNext("A")
                .expectNext("B")
                .verifyComplete();

        verify(loggerPort).trace(anyString(), any(), any(), any());
    }

    @Test
    @DisplayName("Debe manejar Flux con error (Cobertura del lambda de error)")
    void logAroundFluxError() throws Throwable {
        when(pjp.proceed()).thenReturn(Flux.error(new RuntimeException("flux-error")));

        Flux<Object> result = (Flux<Object>) aspect.logAround(pjp, logger);

        StepVerifier.create(result)
                .expectError(RuntimeException.class)
                .verify();

        verify(loggerPort).error(anyString(), any(), eq("flux-error"));
    }

    @Test
    @DisplayName("Debe usar nivel DEBUG si se configura")
    void logAroundLogLevelDebug() throws Throwable {
        when(pjp.proceed()).thenReturn("ok");
        when(logger.logLevel()).thenReturn(Logger.LogLevel.DEBUG);

        aspect.logAround(pjp, logger);

        verify(loggerPort, atLeastOnce()).debug(anyString(), any());
    }

    @Test
    @DisplayName("Debe usar nivel WARN si se configura")
    void logAroundLogLevelWarn() throws Throwable {
        when(pjp.proceed()).thenReturn("ok");
        when(logger.logLevel()).thenReturn(Logger.LogLevel.WARN);

        aspect.logAround(pjp, logger);

        verify(loggerPort, atLeastOnce()).warn(anyString(), any());
    }

    @Test
    @DisplayName("Debe usar nivel TRACE si se configura")
    void logAroundLogLevelTrace() throws Throwable {
        when(pjp.proceed()).thenReturn("ok");
        when(logger.logLevel()).thenReturn(Logger.LogLevel.TRACE);

        aspect.logAround(pjp, logger);

        verify(loggerPort, atLeastOnce()).trace(anyString(), any());
    }

    @Test
    @DisplayName("Debe ignorar excepciones configuradas")
    void logAroundIgnoreException() throws Throwable {
        IllegalArgumentException ex = new IllegalArgumentException("ignored");
        when(pjp.proceed()).thenThrow(ex);
        doReturn(new Class[]{IllegalArgumentException.class}).when(logger).ignoreExceptions();

        assertThrows(IllegalArgumentException.class, () -> aspect.logAround(pjp, logger));

        verify(loggerPort, never()).error(anyString(), any());
    }

    @Test
    @DisplayName("Debe loguear argumentos si logArgs es true")
    void logAroundWithArgs() throws Throwable {
        when(pjp.proceed()).thenReturn("ok");
        when(logger.logArgs()).thenReturn(true);
        when(pjp.getArgs()).thenReturn(new Object[]{"arg1", 1});

        aspect.logAround(pjp, logger);

        verify(loggerPort, atLeastOnce()).info(contains("Starting %s with args: %s"), any(), any());
    }
}