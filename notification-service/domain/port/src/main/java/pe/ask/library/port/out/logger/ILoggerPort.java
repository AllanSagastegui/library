package pe.ask.library.port.out.logger;

public interface ILoggerPort {
    void trace(String message, Object... args);
    void debug(String message, Object... args);
    void info(String message, Object... args);
    void warn(String message, Object... args);
    void error(String message, Object... args);
}
