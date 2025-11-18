package pe.ask.library.api.exception;

import pe.ask.library.model.exception.BaseException;
import pe.ask.library.model.exception.ErrorCatalog;

import java.util.Map;

public class ValidationException extends BaseException {

    public ValidationException(Map<String, String> errors) {
        super(
                ErrorCatalog.VALIDATION_FAILED.getErrorCode(),
                ErrorCatalog.VALIDATION_FAILED.getExceptionName(),
                ErrorCatalog.VALIDATION_FAILED.getMessage(),
                ErrorCatalog.VALIDATION_FAILED.getStatus(),
                errors
        );
    }
}