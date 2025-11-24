package pe.ask.library.port.in.usecase.user;

import pe.ask.library.port.utils.Status;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@FunctionalInterface
public interface IProcessGetUserInfoUseCase {
    Mono<Void> processGetUserInfo(UUID loanId,
                           String userId,
                           LocalDateTime loanDate,
                           LocalDateTime estimatedReturnDate,
                           Status status);
}
