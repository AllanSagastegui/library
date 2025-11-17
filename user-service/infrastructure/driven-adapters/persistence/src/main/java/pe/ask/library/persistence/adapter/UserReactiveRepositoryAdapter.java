package pe.ask.library.persistence.adapter;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import pe.ask.library.model.user.User;
import pe.ask.library.persistence.document.UserDocument;
import pe.ask.library.persistence.helper.AdapterOperations;
import pe.ask.library.persistence.repository.IUserReactiveRepository;
import pe.ask.library.port.out.persistence.IUserRepository;
import reactor.core.publisher.Mono;

@Repository
public class UserReactiveRepositoryAdapter extends AdapterOperations<
        User,
        UserDocument,
        String,
        IUserReactiveRepository
        > implements IUserRepository {

    public UserReactiveRepositoryAdapter(IUserReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, User.class));
    }

    @Override
    public Mono<User> registerUser(User user) {
        return super.repository.save(toData(user))
                .map(this::toEntity);
    }

    @Override
    public Mono<User> getByEmail(String email) {
        return super.repository.findByEmail(email)
                .map(this::toEntity);
    }

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return super.repository.existsByEmail(email);
    }
}
