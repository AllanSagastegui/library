package pe.ask.library.usecase.user;

import pe.ask.library.model.token.Token;
import pe.ask.library.port.in.usecase.user.ILoginUserUseCase;
import pe.ask.library.port.out.kafka.KafkaSender;
import pe.ask.library.port.out.logger.Logger;
import pe.ask.library.port.out.persistence.IUserRepository;
import pe.ask.library.port.out.security.IPasswordEncoder;
import pe.ask.library.port.out.security.ITokenProvider;
import pe.ask.library.usecase.utils.UseCase;
import pe.ask.library.usecase.utils.exception.InvalidCredentialsException;
import reactor.core.publisher.Mono;

@UseCase
public class LoginUserUseCase implements ILoginUserUseCase {

    private final IUserRepository userRepository;
    private final IPasswordEncoder passwordEncoder;
    private final ITokenProvider tokenProvider;

    public LoginUserUseCase(
            IUserRepository userRepository,
            IPasswordEncoder passwordEncoder,
            ITokenProvider tokenProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @Logger
    @Override
    @KafkaSender(topic = "audit-log")
    public Mono<Token> loginUser(String email, String password) {
        return userRepository.getByEmail(email)
                .switchIfEmpty(Mono.error(InvalidCredentialsException::new))
                .flatMap(user ->
                        passwordEncoder.matches(password, user.getPassword())
                                .filter(Boolean::booleanValue)
                                .switchIfEmpty(Mono.error(InvalidCredentialsException::new))
                                .thenReturn(user)
                )
                .flatMap(tokenProvider::generateToken);
    }
}
