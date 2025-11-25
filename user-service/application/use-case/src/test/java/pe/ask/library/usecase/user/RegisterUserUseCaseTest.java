package pe.ask.library.usecase.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.ask.library.model.role.Role;
import pe.ask.library.model.user.User;
import pe.ask.library.port.out.kafka.IKafkaMessageSenderPort;
import pe.ask.library.port.out.persistence.IRoleRepository;
import pe.ask.library.port.out.persistence.IUserRepository;
import pe.ask.library.port.out.security.IPasswordEncoder;
import pe.ask.library.usecase.utils.Roles;
import pe.ask.library.usecase.utils.exception.RoleNotFoundException;
import pe.ask.library.usecase.utils.exception.UserAlreadyExistsException;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private IUserRepository userRepository;
    @Mock
    private IRoleRepository roleRepository;
    @Mock
    private IPasswordEncoder passwordEncoder;
    @Mock
    private IKafkaMessageSenderPort kafkaSender;

    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
        registerUserUseCase = new RegisterUserUseCase(
                userRepository,
                roleRepository,
                passwordEncoder,
                kafkaSender
        );
    }

    @Test
    @DisplayName("Should register user successfully and send welcome email")
    void registerUserSuccess() {
        User inputUser = User.builder()
                .name("John")
                .lastName("Doe")
                .email("john@email.com")
                .password("rawPassword")
                .build();

        Role adminRole = Role.builder()
                .id("role-id-1")
                .name(Roles.ADMIN.toString())
                .build();

        User savedUser = User.builder()
                .id("user-id-1")
                .name("John")
                .lastName("Doe")
                .email("john@email.com")
                .roleId("role-id-1")
                .build();

        when(roleRepository.getRoleByName(Roles.ADMIN.toString()))
                .thenReturn(Mono.just(adminRole));
        when(userRepository.existsByEmail(inputUser.getEmail()))
                .thenReturn(Mono.just(false));
        when(passwordEncoder.encode(inputUser.getPassword()))
                .thenReturn(Mono.just("encodedPassword"));
        when(userRepository.registerUser(any(User.class)))
                .thenReturn(Mono.just(savedUser));
        when(kafkaSender.send(eq("welcome-email"), any()))
                .thenReturn(Mono.empty());

        StepVerifier.create(registerUserUseCase.registerUser(inputUser))
                .expectNext(savedUser)
                .verifyComplete();

        verify(kafkaSender).send(eq("welcome-email"), any());
        verify(userRepository).registerUser(any(User.class));
    }

    @Test
    @DisplayName("Should return RoleNotFoundException when default role (ADMIN) is missing")
    void registerUserRoleNotFound() {
        User inputUser = User.builder()
                .email("john@email.com")
                .build();

        when(roleRepository.getRoleByName(Roles.ADMIN.toString()))
                .thenReturn(Mono.empty());

        when(userRepository.existsByEmail(anyString()))
                .thenReturn(Mono.just(false));

        StepVerifier.create(registerUserUseCase.registerUser(inputUser))
                .expectError(RoleNotFoundException.class)
                .verify();
        verify(kafkaSender, never()).send(anyString(), any());
    }

    @Test
    @DisplayName("Should return UserAlreadyExistsException when email is already registered")
    void registerUserAlreadyExists() {
        User inputUser = User.builder()
                .email("john@email.com")
                .build();

        Role adminRole = Role.builder()
                .id("role-id-1")
                .name(Roles.ADMIN.toString())
                .build();

        when(roleRepository.getRoleByName(Roles.ADMIN.toString()))
                .thenReturn(Mono.just(adminRole));
        when(userRepository.existsByEmail(inputUser.getEmail()))
                .thenReturn(Mono.just(true));

        StepVerifier.create(registerUserUseCase.registerUser(inputUser))
                .expectError(UserAlreadyExistsException.class)
                .verify();

        verify(userRepository, never()).registerUser(any(User.class));
        verify(kafkaSender, never()).send(anyString(), any());
    }

    @Test
    @DisplayName("Should return an error when password encoding fails")
    void registerUserPasswordEncoderFails() {
        User inputUser = User.builder()
                .email("john@email.com")
                .password("rawPassword")
                .build();

        Role adminRole = Role.builder()
                .id("role-id-1")
                .name(Roles.ADMIN.toString())
                .build();

        when(roleRepository.getRoleByName(Roles.ADMIN.toString()))
                .thenReturn(Mono.just(adminRole));
        when(userRepository.existsByEmail(inputUser.getEmail()))
                .thenReturn(Mono.just(false));
        when(passwordEncoder.encode(inputUser.getPassword()))
                .thenReturn(Mono.error(new RuntimeException("Encoding error")));

        StepVerifier.create(registerUserUseCase.registerUser(inputUser))
                .expectErrorSatisfies(throwable -> {
                    assert throwable instanceof RuntimeException;
                    assert throwable.getMessage().equals("Encoding error");
                })
                .verify();

        verify(userRepository, never()).registerUser(any(User.class));
        verify(kafkaSender, never()).send(anyString(), any());
    }

    @Test
    @DisplayName("Should return an error when user repository fails to save")
    void registerUserRepositoryFails() {
        User inputUser = User.builder()
                .email("john@email.com")
                .password("rawPassword")
                .build();

        Role adminRole = Role.builder()
                .id("role-id-1")
                .name(Roles.ADMIN.toString())
                .build();

        when(roleRepository.getRoleByName(Roles.ADMIN.toString()))
                .thenReturn(Mono.just(adminRole));
        when(userRepository.existsByEmail(inputUser.getEmail()))
                .thenReturn(Mono.just(false));
        when(passwordEncoder.encode(inputUser.getPassword()))
                .thenReturn(Mono.just("encodedPassword"));
        when(userRepository.registerUser(any(User.class)))
                .thenReturn(Mono.error(new RuntimeException("DB error")));

        StepVerifier.create(registerUserUseCase.registerUser(inputUser))
                .expectErrorSatisfies(throwable -> {
                    assert throwable instanceof RuntimeException;
                    assert throwable.getMessage().equals("DB error");
                })
                .verify();

        verify(kafkaSender, never()).send(anyString(), any());
    }

    @Test
    @DisplayName("Should return an error when Kafka message sending fails")
    void registerUserKafkaFails() {
        User inputUser = User.builder()
                .name("John")
                .lastName("Doe")
                .email("john@email.com")
                .password("rawPassword")
                .build();

        Role adminRole = Role.builder()
                .id("role-id-1")
                .name(Roles.ADMIN.toString())
                .build();

        User savedUser = User.builder()
                .id("user-id-1")
                .name("John")
                .lastName("Doe")
                .email("john@email.com")
                .roleId("role-id-1")
                .build();

        when(roleRepository.getRoleByName(Roles.ADMIN.toString()))
                .thenReturn(Mono.just(adminRole));
        when(userRepository.existsByEmail(inputUser.getEmail()))
                .thenReturn(Mono.just(false));
        when(passwordEncoder.encode(inputUser.getPassword()))
                .thenReturn(Mono.just("encodedPassword"));
        when(userRepository.registerUser(any(User.class)))
                .thenReturn(Mono.just(savedUser));
        when(kafkaSender.send(eq("welcome-email"), any()))
                .thenReturn(Mono.error(new RuntimeException("Kafka error")));

        StepVerifier.create(registerUserUseCase.registerUser(inputUser))
                .expectErrorSatisfies(throwable -> {
                    assert throwable instanceof RuntimeException;
                    assert throwable.getMessage().equals("Kafka error");
                })
                .verify();
    }
}