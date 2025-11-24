package pe.ask.library.exception;

public class AccessDeniedException extends BaseException {
    public AccessDeniedException() {
        super(
                ErrorCatalog.ACCESS_DENIED.getErrorCode(),
                ErrorCatalog.ACCESS_DENIED.getTitle(),
                ErrorCatalog.ACCESS_DENIED.getMessage(),
                ErrorCatalog.ACCESS_DENIED.getStatus(),
                ErrorCatalog.ACCESS_DENIED.getErrors()
        );
    }
}