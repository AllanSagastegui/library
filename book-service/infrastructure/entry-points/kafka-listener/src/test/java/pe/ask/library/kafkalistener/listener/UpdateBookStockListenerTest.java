package pe.ask.library.kafkalistener.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.kafkalistener.payload.UpdateBook;
import pe.ask.library.kafkalistener.utils.ListenTopics;
import pe.ask.library.port.in.usecase.kafka.IUpdateBookStockUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateBookStockListenerTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private IUpdateBookStockUseCase useCase;

    @InjectMocks
    private UpdateBookStockListener listener;

    @Test
    @DisplayName("Should return correct target topic")
    void getTargetTopic() {
        assertEquals(ListenTopics.LOAN_UPDATE_BOOK_STOCK, listener.getTargetTopic());
    }

    @Test
    @DisplayName("Should return correct payload class")
    void getPayloadClass() {
        assertEquals(UpdateBook.class, listener.getPayloadClass());
    }

    @Test
    @DisplayName("Should process record and call use case successfully")
    void processRecordSuccess() {
        UpdateBook payload = mock(UpdateBook.class);
        UUID bookId = UUID.randomUUID();
        int quantity = 5;
        boolean isIncrement = true;

        when(payload.bookId()).thenReturn(bookId);
        when(payload.quantity()).thenReturn(quantity);
        when(payload.isIncrement()).thenReturn(isIncrement);

        when(useCase.updateBookStock(bookId, quantity, isIncrement)).thenReturn(Mono.empty());

        StepVerifier.create(listener.processRecord(payload))
                .verifyComplete();

        verify(useCase).updateBookStock(bookId, quantity, isIncrement);
    }

    @Test
    @DisplayName("Should propagate error if use case fails")
    void processRecordError() {
        UpdateBook payload = mock(UpdateBook.class);
        when(payload.bookId()).thenReturn(UUID.randomUUID());

        when(useCase.updateBookStock(any(), anyInt(), anyBoolean()))
                .thenReturn(Mono.error(new RuntimeException("Stock update failed")));

        StepVerifier.create(listener.processRecord(payload))
                .expectErrorMessage("Stock update failed")
                .verify();
    }
}