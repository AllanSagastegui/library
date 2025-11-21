package pe.ask.library.aop.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pe.ask.library.port.out.logger.ILoggerPort;
import pe.ask.library.port.out.logger.Logger;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggerAspect {

    private final ILoggerPort loggerPort;

    @Around("@annotation(logger)")
    public Object logAround(ProceedingJoinPoint joinPoint, Logger logger) throws Throwable {

        long startTime = System.nanoTime();
        String methodName = joinPoint.getSignature().toShortString();

        String logPrefix = buildLogPrefix(methodName, logger);

        logStart(joinPoint, logger, logPrefix);

        Object result;
        try {
            result = joinPoint.proceed();

        } catch (Throwable e) {
            logException(logPrefix, e, logger);
            throw e;
        }

        if (result instanceof Mono<?>) {
            return handleMono((Mono<?>) result, logPrefix, logger, startTime);
        }

        if (result instanceof Flux<?>) {
            return handleFlux((Flux<?>) result, logPrefix, logger, startTime);
        }

        handleImperative(result, logPrefix, logger, startTime);
        return result;
    }

    private Mono<?> handleMono(Mono<?> mono, String logPrefix, Logger logger, long startTime) {
        return mono
                .doOnSuccess(res -> logResult(logPrefix, res, logger))
                .doOnError(err -> logException(logPrefix, err, logger))
                .doFinally(signal -> logTime(logPrefix, startTime, logger.unit()));
    }

    private Flux<?> handleFlux(Flux<?> flux, String logPrefix, Logger logger, long startTime) {

        return flux
                .doOnError(err ->
                        logException(logPrefix, err, logger)
                )
                .doFinally(signal ->
                        logTime(logPrefix, startTime, logger.unit())
                );
    }

    private void handleImperative(Object result, String logPrefix, Logger logger, long startTime) {
        logResult(logPrefix, result, logger);
        logTime(logPrefix, startTime, logger.unit());
    }

    private String buildLogPrefix(String methodName, Logger logger) {
        StringBuilder prefix = new StringBuilder();
        if (logger.includeInvocationId()) {
            prefix.append("[").append(generateId()).append("] ");
        }
        if (logger.logThread()) {
            prefix.append("[").append(Thread.currentThread().getName()).append("] ");
        }
        prefix.append(methodName);
        return prefix.toString();
    }

    private void logStart(ProceedingJoinPoint joinPoint, Logger logger, String logPrefix) {
        if (logger.logArgs()) {
            Object[] args = joinPoint.getArgs();
            log(logger.logLevel(), "Starting %s with args: %s", logPrefix, Arrays.toString(args));
        } else {
            log(logger.logLevel(), "Starting %s", logPrefix);
        }
    }

    private void logResult(String logPrefix, Object res, Logger logger) {
        if (logger.logResult()) {
            String resultString = (res != null) ? res.toString() : "null";
            if (resultString.length() > 150) {
                resultString = resultString.substring(0, 150) + "...(truncated)";
            }
            log(logger.logLevel(), "%s finished with result: %s", logPrefix, resultString);
        } else {
            log(logger.logLevel(), "%s finished", logPrefix);
        }
    }

    private void logTime(String logPrefix, long startTimeNanos, TimeUnit unit) {
        long elapsed = System.nanoTime() - startTimeNanos;
        long convertedElapsed = unit.convert(elapsed, TimeUnit.NANOSECONDS);

        loggerPort.trace("%s took %d %s", logPrefix, convertedElapsed, unit.name().toLowerCase());
    }

    private void logException(String logPrefix, Throwable e, Logger logger) {
        if (!logger.logExceptions() || shouldIgnoreException(e, logger.ignoreExceptions())) {
            return;
        }

        if (logger.logExceptionStackTrace()) {
            loggerPort.error("%s failed with exception", logPrefix, e);
        } else {
            loggerPort.error("%s failed: %s", logPrefix, e.getMessage());
        }
    }

    private boolean shouldIgnoreException(Throwable e, Class<? extends Throwable>[] exceptionsToIgnore) {
        for (Class<? extends Throwable> toIgnore : exceptionsToIgnore) {
            if (toIgnore.isInstance(e)) {
                return true;
            }
        }
        return false;
    }

    private void log(Logger.LogLevel level, String message, Object... args) {
        switch (level) {
            case TRACE:
                loggerPort.trace(message, args);
                break;
            case DEBUG:
                loggerPort.debug(message, args);
                break;
            case INFO:
                loggerPort.info(message, args);
                break;
            case WARN:
                loggerPort.warn(message, args);
                break;
            case ERROR:
                loggerPort.error(message, args);
                break;
        }
    }

    private String generateId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
