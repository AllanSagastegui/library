package pe.ask.library.usecase.utils.exception;

import pe.ask.library.model.exception.BaseException;
import pe.ask.library.model.exception.ErrorCatalog;

public class InvalidCredentialsException extends BaseException {
    public InvalidCredentialsException() {
        super(
                ErrorCatalog.INVALID_CREDENTIALS.getErrorCode(),
                ErrorCatalog.INVALID_CREDENTIALS.getExceptionName(),
                ErrorCatalog.INVALID_CREDENTIALS.getMessage(),
                ErrorCatalog.INVALID_CREDENTIALS.getStatus(),
                ErrorCatalog.INVALID_CREDENTIALS.getErrors()
        );
    }
}
