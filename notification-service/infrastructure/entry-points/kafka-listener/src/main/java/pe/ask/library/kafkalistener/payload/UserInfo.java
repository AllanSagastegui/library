package pe.ask.library.kafkalistener.payload;

import pe.ask.library.port.utils.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserInfo(
        UUID loanId,
        String email,
        String completeName,
        LocalDateTime loanDate,
        LocalDateTime estimatedReturnDate,
        Status status
){ }