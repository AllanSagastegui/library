package pe.ask.library.port.out.persistence;

import pe.ask.library.model.role.Role;
import reactor.core.publisher.Mono;

public interface IRoleRepository {
    Mono<Role> getRoleById(String id);
    Mono<Role> getRoleByName(String name);
}
