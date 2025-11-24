package pe.ask.library.kafkalistener.payload;

import java.util.UUID;

public record ValidateStock(
        UUID loanId,
        UUID bookId
) {
}
