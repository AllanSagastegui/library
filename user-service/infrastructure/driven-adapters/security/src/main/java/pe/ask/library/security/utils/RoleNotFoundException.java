package pe.ask.library.security.utils;

import pe.ask.library.model.exception.BaseException;
import pe.ask.library.model.exception.ErrorCatalog;

public class RoleNotFoundException extends BaseException {
    public RoleNotFoundException() {
        super(
                ErrorCatalog.ROLE_NOT_FOUND.getErrorCode(),
                ErrorCatalog.ROLE_NOT_FOUND.getExceptionName(),
                ErrorCatalog.ROLE_NOT_FOUND.getMessage(),
                ErrorCatalog.ROLE_NOT_FOUND.getStatus(),
                ErrorCatalog.ROLE_NOT_FOUND.getErrors()
        );
    }
}
