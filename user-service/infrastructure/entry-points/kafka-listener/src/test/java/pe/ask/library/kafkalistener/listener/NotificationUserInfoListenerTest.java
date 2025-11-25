package pe.ask.library.kafkalistener.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.kafkalistener.payload.GetUserInfo;
import pe.ask.library.port.in.usecase.user.IProcessGetUserInfoUseCase;
import pe.ask.library.port.utils.Status;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationUserInfoListenerTest {

    @Mock
    private ObjectMapper mapper;

    @Mock
    private IProcessGetUserInfoUseCase useCase;

    @InjectMocks
    private NotificationUserInfoListener listener;

    @Test
    @DisplayName("Should return correct target topic")
    void getTargetTopic() {
        assertEquals("notification-user-info", listener.getTargetTopic());
    }

    @Test
    @DisplayName("Should return correct payload class")
    void getPayloadClass() {
        assertEquals(GetUserInfo.class, listener.getPayloadClass());
    }

    @Test
    @DisplayName("Should call use case with correct mapped data when processing record")
    void processRecordSuccess() {
        GetUserInfo payload = mock(GetUserInfo.class);

        UUID loanId = UUID.randomUUID();
        String userId = "user-123";
        LocalDateTime now = LocalDateTime.now();
        Status status = Status.PENDING;

        when(payload.loanId()).thenReturn(loanId);
        when(payload.userId()).thenReturn(userId);
        when(payload.loanDate()).thenReturn(now);
        when(payload.estimatedReturnDate()).thenReturn(now.plusDays(7));
        when(payload.status()).thenReturn(status);

        when(useCase.processGetUserInfo(
                eq(loanId),
                eq(userId),
                eq(now),
                any(LocalDateTime.class),
                eq(status)
        )).thenReturn(Mono.empty());

        StepVerifier.create(listener.processRecord(payload))
                .verifyComplete();

        verify(useCase).processGetUserInfo(
                eq(loanId),
                eq(userId),
                eq(now),
                any(LocalDateTime.class),
                eq(status)
        );
    }

    @Test
    @DisplayName("Should propagate error if use case fails")
    void processRecordError() {
        GetUserInfo payload = mock(GetUserInfo.class);

        when(payload.loanId()).thenReturn(UUID.randomUUID());

        when(useCase.processGetUserInfo(any(), any(), any(), any(), any()))
                .thenReturn(Mono.error(new RuntimeException("Use case failed")));

        StepVerifier.create(listener.processRecord(payload))
                .expectErrorMessage("Use case failed")
                .verify();
    }
}