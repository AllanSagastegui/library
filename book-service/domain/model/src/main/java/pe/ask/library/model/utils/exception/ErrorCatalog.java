package pe.ask.library.model.utils.exception;

import java.util.Map;

public enum ErrorCatalog {
    VALIDATION_EXCEPTION(
            "AUTH_VALIDATION_EXCEPTION",
            "Validation Failed",
            "Oops! Some of the data you sent doesn’t look right. Please review the fields and try again.",
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

    COURSE_ALREADY_EXISTS(
            "COURSE_ALREADY_EXISTS",
            "CourseAlreadyExistsException",
            "A course with the same code or name already exists.",
            409,
            null
    ),
    COURSE_NOT_FOUND(
            "COURSE_NOT_FOUND",
            "CourseNotFoundException",
            "The requested course could not be found.",
            404,
            null
    ),

    ENROLLMENT_NOT_FOUND(
            "ENROLLMENT_NOT_FOUND",
            "EnrollmentNotFoundException",
            "The enrollment record you are looking for does not exist.",
            404,
            null
    ),

    PERIOD_ALREADY_EXISTS(
            "PERIOD_ALREADY_EXISTS",
            "PeriodAlreadyExistsException",
            "A period with the same identifier already exists.",
            409,
            null
    ),
    PERIOD_NOT_FOUND(
            "PERIOD_NOT_FOUND",
            "PeriodNotFoundException",
            "The academic period could not be found.",
            404,
            null
    ),

    STUDENT_ALREADY_EXISTS(
            "STUDENT_ALREADY_EXISTS",
            "StudentAlreadyExistsException",
            "A student with the same document or code already exists.",
            409,
            null
    ),
    STUDENT_NOT_FOUND(
            "STUDENT_NOT_FOUND",
            "StudentNotFoundException",
            "The requested student could not be found.",
            404,
            null
    )

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
