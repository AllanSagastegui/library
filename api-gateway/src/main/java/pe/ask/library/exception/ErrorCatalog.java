package pe.ask.library.exception;

import java.util.Map;

public enum ErrorCatalog {
    INTERNAL_SERVER_ERROR(
            "APPLICATIONS_INTERNAL_SERVER_ERROR",
            "Internal Server Error",
            "Something went wrong on our side. Please try again later or contact support if the issue persists.",
            500,
            Map.of("server", "Unexpected error occurred")
    ),
    UNAUTHORIZED(
            "APPLICATIONS_UNAUTHORIZED",
            "Unauthorized",
            "You are not authenticated. Please log in to access this resource.",
            401,
            Map.of("user", "Authentication required")
    ),
    ACCESS_DENIED(
            "APPLICATIONS_ACCESS_DENIED",
            "Access Denied",
            "You do not have permission to access this resource.",
            403,
            Map.of("user", "You lack the necessary permissions")
    ),



    ;
    private final String errorCode;
    private final String title;
    private final String message;
    private final int status;
    private final Map<String, String> errors;

    ErrorCatalog(String errorCode, String title, String message, int status, Map<String, String> errors) {
        this.errorCode = errorCode;
        this.title = title;
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
