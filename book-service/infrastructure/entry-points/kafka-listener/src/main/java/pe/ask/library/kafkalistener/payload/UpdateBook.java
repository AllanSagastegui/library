package pe.ask.library.kafkalistener.payload;

import java.util.UUID;

public record UpdateBook(
        UUID bookId,
        int quantity,
        boolean isIncrement
) {
}
