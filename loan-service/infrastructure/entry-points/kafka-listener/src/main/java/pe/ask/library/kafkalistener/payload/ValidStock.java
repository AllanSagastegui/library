package pe.ask.library.kafkalistener.payload;

import java.util.UUID;

public record ValidStock(
        UUID loanId,
        UUID bookId,
        Boolean isValid
){}