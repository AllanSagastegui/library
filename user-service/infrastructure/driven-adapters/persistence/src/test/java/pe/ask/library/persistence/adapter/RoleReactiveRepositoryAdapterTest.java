package pe.ask.library.persistence.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import pe.ask.library.model.role.Role;
import pe.ask.library.persistence.document.RoleDocument;
import pe.ask.library.persistence.repository.IRoleReactiveRepository;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleReactiveRepositoryAdapterTest {

    @Mock
    private IRoleReactiveRepository repository;

    @Mock
    private ObjectMapper mapper;

    private RoleReactiveRepositoryReactiveAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new RoleReactiveRepositoryReactiveAdapter(repository, mapper);
    }

    @Test
    @DisplayName("Should find role by ID and return mapped entity")
    void getRoleByIdSuccess() {
        String id = "role-123";
        RoleDocument document = new RoleDocument();
        Role role = new Role();

        when(repository.findById(id)).thenReturn(Mono.just(document));
        when(mapper.map(document, Role.class)).thenReturn(role);

        StepVerifier.create(adapter.getRoleById(id))
                .expectNext(role)
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return empty when role ID not found")
    void getRoleByIdEmpty() {
        String id = "unknown";
        when(repository.findById(id)).thenReturn(Mono.empty());

        StepVerifier.create(adapter.getRoleById(id))
                .verifyComplete();
    }

    @Test
    @DisplayName("Should find role by name and return mapped entity")
    void getRoleByNameSuccess() {
        String name = "ADMIN";
        RoleDocument document = new RoleDocument();
        Role role = new Role();

        when(repository.findByName(name)).thenReturn(Mono.just(document));
        when(mapper.map(document, Role.class)).thenReturn(role);

        StepVerifier.create(adapter.getRoleByName(name))
                .expectNext(role)
                .verifyComplete();
    }
}
