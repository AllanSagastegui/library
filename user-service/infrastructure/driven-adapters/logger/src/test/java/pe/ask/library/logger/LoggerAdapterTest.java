package pe.ask.library.logger;

import com.github.valfirst.slf4jtest.TestLogger;
import com.github.valfirst.slf4jtest.TestLoggerFactory;
import com.github.valfirst.slf4jtest.LoggingEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoggerAdapterTest {

    private final TestLogger testLogger = TestLoggerFactory.getTestLogger(LoggerAdapter.class);

    private LoggerAdapter loggerAdapter;

    @BeforeEach
    void setUp() {
        testLogger.clearAll();
        loggerAdapter = new LoggerAdapter();
    }

    @AfterEach
    void tearDown() {
        testLogger.clearAll();
    }

    @Test
    void trace() {
        loggerAdapter.trace("trace message %s", "test");

        List<LoggingEvent> events = testLogger.getLoggingEvents();
        assertEquals(1, events.size());
        assertEquals(Level.TRACE, events.get(0).getLevel());
        assertEquals("trace message test", events.get(0).getMessage());
    }

    @Test
    void debug() {
        loggerAdapter.debug("debug message");

        List<LoggingEvent> events = testLogger.getLoggingEvents();
        assertEquals(1, events.size());
        assertEquals(Level.DEBUG, events.get(0).getLevel());
        assertEquals("debug message", events.get(0).getMessage());
    }

    @Test
    void info() {
        loggerAdapter.info("info message %d", 123);

        List<LoggingEvent> events = testLogger.getLoggingEvents();
        assertEquals(1, events.size());
        assertEquals(Level.INFO, events.get(0).getLevel());
        assertEquals("info message 123", events.get(0).getMessage());
    }

    @Test
    void warn() {
        loggerAdapter.warn("warn message");

        List<LoggingEvent> events = testLogger.getLoggingEvents();
        assertEquals(1, events.size());
        assertEquals(Level.WARN, events.get(0).getLevel());
        assertEquals("warn message", events.get(0).getMessage());
    }

    @Test
    void error() {
        loggerAdapter.error("error message");

        List<LoggingEvent> events = testLogger.getLoggingEvents();
        assertEquals(1, events.size());
        assertEquals(Level.ERROR, events.get(0).getLevel());
        assertEquals("error message", events.get(0).getMessage());
    }

    @Test
    void formatExceptionFallback() {
        loggerAdapter.info("Hello %d", "NotANumber");

        List<LoggingEvent> events = testLogger.getLoggingEvents();
        assertEquals(1, events.size());
        assertEquals("Hello %d", events.get(0).getMessage());
    }

    @Test
    void formatNullArgs() {
        loggerAdapter.info("Hello World", (Object[]) null);

        List<LoggingEvent> events = testLogger.getLoggingEvents();
        assertEquals(1, events.size());
        assertEquals("Hello World", events.get(0).getMessage());
    }

    @Test
    void formatEmptyArgs() {
        loggerAdapter.info("Hello World", new Object[]{});

        List<LoggingEvent> events = testLogger.getLoggingEvents();
        assertEquals(1, events.size());
        assertEquals("Hello World", events.get(0).getMessage());
    }
}