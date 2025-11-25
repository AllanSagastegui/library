package pe.ask.library.kafkalistener.payload;

import pe.ask.library.port.utils.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetUserInfo(
        UUID loanId,
        String userId,
        LocalDateTime loanDate,
        LocalDateTime estimatedReturnDate,
        Status status
){ }
