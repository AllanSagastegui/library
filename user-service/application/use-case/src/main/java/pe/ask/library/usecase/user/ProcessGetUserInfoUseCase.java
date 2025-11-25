package pe.ask.library.usecase.user;

import pe.ask.library.port.in.usecase.user.IProcessGetUserInfoUseCase;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import pe.ask.library.port.out.persistence.IUserRepository;
import pe.ask.library.port.utils.Status;
import pe.ask.library.usecase.utils.UseCase;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@UseCase
public class ProcessGetUserInfoUseCase implements IProcessGetUserInfoUseCase {

    private final IUserRepository repository;
    private final IKafkaMessageSenderPort kafkaSender;

    public ProcessGetUserInfoUseCase(
            IUserRepository repository,
            IKafkaMessageSenderPort kafkaSender
    ) {
        this.repository = repository;
        this.kafkaSender = kafkaSender;
    }

    @Override
    public Mono<Void> processGetUserInfo(UUID loanId,
                                         String userId,
                                         LocalDateTime loanDate,
                                         LocalDateTime estimatedReturnDate,
                                         Status status) {
        return repository.getUserById(userId)
                .switchIfEmpty(Mono.error(RuntimeException::new))
                .flatMap(user -> {
                    UserInfo userInfo = new UserInfo(
                            loanId,
                            user.getEmail(),
                            user.getName() + " " + user.getLastName(),
                            loanDate,
                            estimatedReturnDate,
                            status
                    );
                    return kafkaSender.send("loan-email", userInfo);
                });
    }

    private record UserInfo(
            UUID loanId,
            String email,
            String completeName,
            LocalDateTime loanDate,
            LocalDateTime estimatedReturnDate,
            Status status
    ){ }
}
