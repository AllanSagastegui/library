package pe.ask.library.model.exception;

import java.util.Map;

public enum ErrorCatalog {
    USER_ALREADY_EXISTS(
            "AUTH_USER_ALREADY_EXISTS",
            "User Already Exists",
            "A user with this information already exists. Please use different information to Sign Up.",
            409,
            Map.of("email or National ID", "This email or National ID is already in use.")
    ),
    ROLE_NOT_FOUND(
            "AUTH_ROLE_NOT_FOUND",
            "Role Not Found",
            "The role you specified does not exist. Please check and try again.",
            404,
            Map.of("role", "The specified role does not exist.")
    ),
    INVALID_CREDENTIALS(
            "AUTH_INVALID_CREDENTIALS",
            "Invalid Credentials",
            "The credentials you provided are incorrect. Please try again",
            401,
            Map.of("credentials", "Invalid email or password")
    ),
    VALIDATION_FAILED(
            "AUTH_VALIDATION_FAILED",
            "Validation Failed",
            "Oops! Some of the data you send does not look rigth. Please review the fields and try again.",
            400,
            null
    ),
    INTERNAL_SERVER_ERROR(
            "AUTH_INTERNAL_SERVER_ERROR",
            "Internal Server Error",
            "Something went wrong on our side. Please try again later or contact support if the issue persists.",
            500,
            Map.of("server", "Unexpected error occurred")
    ),
    ;

    private final String errorCode;
    private final String exceptionName;
    private final String message;
    private final int status;
    private final Map<String, String> errors;

    ErrorCatalog(String errorCode, String exceptionName, String message, int status, Map<String, String> errors) {
        this.errorCode = errorCode;
        this.exceptionName = exceptionName;
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getExceptionName() {
        return exceptionName;
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
