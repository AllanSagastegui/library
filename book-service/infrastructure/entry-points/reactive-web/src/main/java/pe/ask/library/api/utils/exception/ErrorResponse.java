package pe.ask.library.api.utils.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class ErrorResponse {
    private String errorCode;
    private String tittle;
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private Object errors;
}