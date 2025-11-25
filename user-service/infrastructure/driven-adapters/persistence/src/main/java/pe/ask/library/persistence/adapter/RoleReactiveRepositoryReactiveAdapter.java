package pe.ask.library.persistence.adapter;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import pe.ask.library.model.role.Role;
import pe.ask.library.persistence.document.RoleDocument;
import pe.ask.library.persistence.helper.ReactiveAdapterOperations;
import pe.ask.library.persistence.repository.IRoleReactiveRepository;
import pe.ask.library.port.out.persistence.IRoleRepository;
import reactor.core.publisher.Mono;

@Repository
public class RoleReactiveRepositoryReactiveAdapter extends ReactiveAdapterOperations<
        Role,
        RoleDocument,
        String,
        IRoleReactiveRepository
        > implements IRoleRepository {

    public RoleReactiveRepositoryReactiveAdapter(IRoleReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Role.class));
    }

    @Override
    public Mono<Role> getRoleById(String id) {
        return super.repository.findById(id)
                .map(this::toEntity);
    }

    @Override
    public Mono<Role> getRoleByName(String name) {
        return super.repository.findByName(name)
                .map(this::toEntity);
    }
}
