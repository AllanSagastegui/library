package pe.ask.library.kafkalistener.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.kafkalistener.payload.ValidateStock;
import pe.ask.library.kafkalistener.utils.ListenTopics;
import pe.ask.library.port.in.usecase.kafka.IValidateBookStockUseCase;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ValidateBookStockListenerTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private IValidateBookStockUseCase useCase;

    @InjectMocks
    private ValidateBookStockListener listener;

    @Test
    @DisplayName("Should return correct target topic")
    void getTargetTopic() {
        assertEquals(ListenTopics.LOAN_VALIDATE_BOOK_STOCK, listener.getTargetTopic());
    }

    @Test
    @DisplayName("Should return correct payload class")
    void getPayloadClass() {
        assertEquals(ValidateStock.class, listener.getPayloadClass());
    }

    @Test
    @DisplayName("Should process record and call validation use case successfully")
    void processRecordSuccess() {
        ValidateStock payload = mock(ValidateStock.class);
        UUID loanId = UUID.randomUUID();
        UUID bookId = UUID.randomUUID();

        when(payload.loanId()).thenReturn(loanId);
        when(payload.bookId()).thenReturn(bookId);

        when(useCase.validateBookStock(loanId, bookId)).thenReturn(Mono.empty());

        StepVerifier.create(listener.processRecord(payload))
                .verifyComplete();

        verify(useCase).validateBookStock(loanId, bookId);
    }

    @Test
    @DisplayName("Should propagate error if validation use case fails")
    void processRecordError() {
        ValidateStock payload = mock(ValidateStock.class);
        when(payload.loanId()).thenReturn(UUID.randomUUID());

        when(useCase.validateBookStock(any(), any()))
                .thenReturn(Mono.error(new RuntimeException("Validation failed")));

        StepVerifier.create(listener.processRecord(payload))
                .expectErrorMessage("Validation failed")
                .verify();
    }
}