package pe.ask.library.api.utils.exception;

import pe.ask.library.model.utils.exception.BaseException;
import pe.ask.library.model.utils.exception.ErrorCatalog;

public class UnexpectedException extends BaseException {
    public UnexpectedException(Throwable cause) {
        super(
                ErrorCatalog.INTERNAL_SERVER_ERROR.getErrorCode(),
                ErrorCatalog.INTERNAL_SERVER_ERROR.getExceptionName(),
                ErrorCatalog.INTERNAL_SERVER_ERROR.getMessage(),
                ErrorCatalog.INTERNAL_SERVER_ERROR.getStatus(),
                ErrorCatalog.INTERNAL_SERVER_ERROR.getErrors()
        );
        initCause(cause);
    }
}
