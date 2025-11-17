package pe.ask.library.logger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pe.ask.library.port.out.logger.ILoggerPort;

@Slf4j
@Component
public class LoggerAdapter implements ILoggerPort {
    @Override
    public void trace(String message, Object... args) {
        log.trace(format(message, args));
    }

    @Override
    public void debug(String message, Object... args) {
        log.debug(format(message, args));
    }

    @Override
    public void info(String message, Object... args) {
        log.info(format(message, args));
    }

    @Override
    public void warn(String message, Object... args) {
        log.warn(format(message, args));
    }

    @Override
    public void error(String message, Object... args) {
        log.error(format(message, args));
    }

    private String format(String message, Object... args) {
        try {
            return (args != null && args.length > 0)
                    ? String.format(message, args)
                    : message;
        } catch (Exception e) {
            return message;
        }
    }
}
