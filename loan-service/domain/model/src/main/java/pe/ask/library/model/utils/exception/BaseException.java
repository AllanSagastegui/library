package pe.ask.library.model.utils.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class BaseException extends RuntimeException {
    private final String errorCode;
    private final String title;
    private final String message;
    private final int status;
    private final LocalDateTime timestamp;
    private final Map<String, String> errors;

    protected BaseException(String errorCode, String title, String message, int status, Map<String, String> errors) {
        super(message);
        this.errorCode = errorCode;
        this.title = title;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
