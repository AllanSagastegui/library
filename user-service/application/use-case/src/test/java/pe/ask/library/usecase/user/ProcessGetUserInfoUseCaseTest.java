package pe.ask.library.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.user.User;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import pe.ask.library.port.out.persistence.IUserRepository;
import pe.ask.library.port.utils.Status;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessGetUserInfoUseCaseTest {

    @Mock
    private IUserRepository userRepository;
    @Mock
    private IKafkaMessageSenderPort kafkaSender;

    private ProcessGetUserInfoUseCase processGetUserInfoUseCase;

    @BeforeEach
    void setUp() {
        processGetUserInfoUseCase = new ProcessGetUserInfoUseCase(
                userRepository,
                kafkaSender
        );
    }

    @Test
    @DisplayName("Should process user info and send Kafka message successfully")
    void processGetUserInfoSuccess() {
        UUID loanId = UUID.randomUUID();
        String userId = "user-id-1";
        LocalDateTime loanDate = LocalDateTime.now();
        LocalDateTime estimatedReturnDate = loanDate.plusDays(15);
        Status status = Status.LOANED;

        User user = User.builder()
                .id(userId)
                .name("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .build();

        when(userRepository.getUserById(userId)).thenReturn(Mono.just(user));
        when(kafkaSender.send(eq("loan-email"), any())).thenReturn(Mono.empty());

        StepVerifier.create(processGetUserInfoUseCase.processGetUserInfo(loanId, userId, loanDate, estimatedReturnDate, status))
                .verifyComplete();

        verify(kafkaSender).send(eq("loan-email"), any());
    }

    @Test
    @DisplayName("Should return an error when user is not found")
    void processGetUserInfoUserNotFound() {
        UUID loanId = UUID.randomUUID();
        String userId = "non-existent-user";
        LocalDateTime loanDate = LocalDateTime.now();
        LocalDateTime estimatedReturnDate = loanDate.plusDays(15);
        Status status = Status.LOANED;

        when(userRepository.getUserById(userId)).thenReturn(Mono.empty());

        StepVerifier.create(processGetUserInfoUseCase.processGetUserInfo(loanId, userId, loanDate, estimatedReturnDate, status))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("Should return an error when Kafka message sending fails")
    void processGetUserInfoKafkaFails() {
        UUID loanId = UUID.randomUUID();
        String userId = "user-id-1";
        LocalDateTime loanDate = LocalDateTime.now();
        LocalDateTime estimatedReturnDate = loanDate.plusDays(15);
        Status status = Status.LOANED;

        User user = User.builder()
                .id(userId)
                .name("John")
                .lastName("Doe")
                .email("john.doe@email.com")
                .build();

        when(userRepository.getUserById(userId)).thenReturn(Mono.just(user));
        when(kafkaSender.send(eq("loan-email"), any())).thenReturn(Mono.error(new RuntimeException("Kafka error")));

        StepVerifier.create(processGetUserInfoUseCase.processGetUserInfo(loanId, userId, loanDate, estimatedReturnDate, status))
                .expectErrorSatisfies(throwable -> {
                    assert throwable instanceof RuntimeException;
                    assert throwable.getMessage().equals("Kafka error");
                })
                .verify();
    }
}
