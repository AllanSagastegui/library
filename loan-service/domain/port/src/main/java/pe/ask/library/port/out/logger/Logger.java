package pe.ask.library.port.out.logger;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Logger {
    LogLevel logLevel() default LogLevel.TRACE;

    boolean logArgs() default true;
    boolean includeInvocationId() default false;
    boolean logThread() default false;
    boolean logResult() default true;
    boolean logTime() default true;
    TimeUnit unit() default TimeUnit.MILLISECONDS;
    boolean logExceptions() default true;
    boolean logExceptionStackTrace() default false;
    Class<? extends Throwable>[] ignoreExceptions() default {};
    enum LogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR
    }
}
