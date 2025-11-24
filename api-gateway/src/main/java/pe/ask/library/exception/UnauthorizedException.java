package pe.ask.library.exception;

public class UnauthorizedException extends BaseException {
    public UnauthorizedException() {
        super(
                ErrorCatalog.UNAUTHORIZED.getErrorCode(),
                ErrorCatalog.UNAUTHORIZED.getTitle(),
                ErrorCatalog.UNAUTHORIZED.getMessage(),
                ErrorCatalog.UNAUTHORIZED.getStatus(),
                ErrorCatalog.UNAUTHORIZED.getErrors()
        );
    }
}