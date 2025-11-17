package pe.ask.library.model.exception;

import java.time.LocalDateTime;
import java.util.Map;

public class BaseException extends RuntimeException {
    private final String errorCode;
    private final String exceptionName;
    private final String message;
    private final int status;
    private final LocalDateTime timestamp;
    private final Map<String, String> errors;

    protected BaseException(
            String errorCode,
            String exceptionName,
            String message,
            int status,
            Map<String, String> errors
    ) {
        super(message);
        this.errorCode = errorCode;
        this.exceptionName = exceptionName;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
        this.errors = errors;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getExceptionName() {
        return exceptionName;
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
