package pe.ask.library.usecase.user;

import pe.ask.library.model.user.User;
import pe.ask.library.port.in.usecase.user.IRegisterUserUseCase;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IRoleRepository;
import pe.ask.library.port.out.persistence.IUserRepository;
import pe.ask.library.port.out.security.IPasswordEncoder;
import pe.ask.library.usecase.utils.Roles;
import pe.ask.library.usecase.utils.UseCase;
import pe.ask.library.usecase.utils.exception.RoleNotFoundException;
import pe.ask.library.usecase.utils.exception.UserAlreadyExistsException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@UseCase
public class RegisterUserUseCase implements IRegisterUserUseCase {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IPasswordEncoder passwordEncoder;
    private final IKafkaMessageSenderPort kafkaSender;

    public RegisterUserUseCase(
            IUserRepository userRepository,
            IRoleRepository roleRepository,
            IPasswordEncoder passwordEncoder,
            IKafkaMessageSenderPort kafkaSender
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.kafkaSender = kafkaSender;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<User> registerUser(User user) {
        return roleRepository.getRoleByName(Roles.ADMIN.toString())
                .switchIfEmpty(Mono.error(RoleNotFoundException::new))
                .zipWith(userRepository.existsByEmail(user.getEmail()))
                .filter(tuple -> !tuple.getT2())
                .switchIfEmpty(Mono.error(UserAlreadyExistsException::new))
                .flatMap(tuple -> {
                    var role = tuple.getT1();
                    return passwordEncoder.encode(user.getPassword())
                            .map(encodedPassword ->
                                    User.builder()
                                            .name(user.getName())
                                            .lastName(user.getLastName())
                                            .email(user.getEmail())
                                            .password(encodedPassword)
                                            .createdAt(LocalDateTime.now())
                                            .updatedAt(LocalDateTime.now())
                                            .roleId(role.getId())
                                            .build()
                            );
                })
                .flatMap(userRepository::registerUser)
                .flatMap(userSaved -> {
                    var welcomeEmail = new WelcomeEmail(
                            userSaved.getName() + " " + userSaved.getLastName(),
                            userSaved.getEmail()
                    );
                    return kafkaSender.send("welcome-email", welcomeEmail)
                            .thenReturn(userSaved);
                });
    }

    private record WelcomeEmail(
            String completeName,
            String email
    ){}
}
