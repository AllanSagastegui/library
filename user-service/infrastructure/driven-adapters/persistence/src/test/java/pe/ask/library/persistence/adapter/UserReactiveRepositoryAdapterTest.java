package pe.ask.library.persistence.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import pe.ask.library.model.user.User;
import pe.ask.library.persistence.document.UserDocument;
import pe.ask.library.persistence.repository.IUserReactiveRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserReactiveRepositoryAdapterTest {

    @Mock
    private IUserReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private UserReactiveRepositoryReactiveAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new UserReactiveRepositoryReactiveAdapter(repository, mapper);
    }

    @Test
    @DisplayName("Should register user: convert to data, save, and convert back to entity")
    void registerUserSuccess() {
        User inputUser = User.builder().email("test@email.com").build();
        UserDocument document = new UserDocument();
        UserDocument savedDocument = new UserDocument();
        User savedUser = User.builder().id("generated-id").email("test@email.com").build();

        when(mapper.map(inputUser, UserDocument.class)).thenReturn(document);

        when(repository.save(document)).thenReturn(Mono.just(savedDocument));

        when(mapper.map(savedDocument, User.class)).thenReturn(savedUser);

        StepVerifier.create(adapter.registerUser(inputUser))
                .expectNext(savedUser)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should find user by email and return mapped entity")
    void getByEmailSuccess() {
        String email = "test@email.com";
        UserDocument document = new UserDocument();
        User user = User.builder().email(email).build();

        when(repository.findByEmail(email)).thenReturn(Mono.just(document));
        when(mapper.map(document, User.class)).thenReturn(user);

        StepVerifier.create(adapter.getByEmail(email))
                .expectNext(user)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return true if user exists by email")
    void existsByEmailTrue() {
        String email = "exists@email.com";
        when(repository.existsByEmail(email)).thenReturn(Mono.just(true));

        StepVerifier.create(adapter.existsByEmail(email))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return false if user does not exist by email")
    void existsByEmailFalse() {
        String email = "new@email.com";
        when(repository.existsByEmail(email)).thenReturn(Mono.just(false));

        StepVerifier.create(adapter.existsByEmail(email))
                .expectNext(false)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should get user by ID successfully")
    void getUserByIdSuccess() {
        String userId = "user-123";
        UserDocument document = new UserDocument();
        User user = User.builder().id(userId).build();

        when(repository.findById(userId)).thenReturn(Mono.just(document));
        when(mapper.map(document, User.class)).thenReturn(user);

        StepVerifier.create(adapter.getUserById(userId))
                .expectNext(user)
                .verifyComplete();
    }
}