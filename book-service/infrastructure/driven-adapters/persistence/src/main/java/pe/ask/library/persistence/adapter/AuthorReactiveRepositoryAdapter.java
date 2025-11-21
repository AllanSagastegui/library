package pe.ask.library.persistence.adapter;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import pe.ask.library.model.author.Author;
import pe.ask.library.persistence.entity.AuthorEntity;
import pe.ask.library.persistence.helper.ReactiveAdapterOperations;
import pe.ask.library.persistence.repository.IAuthorReactiveRepository;
import pe.ask.library.port.out.persistence.IAuthorRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class AuthorReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Author,
        AuthorEntity,
        UUID,
        IAuthorReactiveRepository
        > implements IAuthorRepository {

    public AuthorReactiveRepositoryAdapter(IAuthorReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Author.class));
    }

    @Override
    public Flux<Author> getAllAuthors(int page, int size) {
        return super.repository.findAllPaginated(page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Flux<Author> getAllAuthorsByNationality(int page, int size, String nationality) {
        return super.repository.findAllAuthorsByNationality(page * size, size, queryValue(nationality))
                .map(this::toEntity);
    }

    @Override
    public Mono<Author> getAuthorById(UUID id) {
        return super.repository.findById(id)
                .map(this::toEntity);
    }

    @Override
    public Mono<Author> getAuthorByPseudonym(String pseudonym) {
        return super.repository.findAuthorByPseudonymIsContainingIgnoreCase(pseudonym)
                .map(this::toEntity);
    }

    @Override
    public Mono<Author> saveAuthor(Author author) {
        return super.repository.save(toData(author))
                .map(this::toEntity);
    }

    @Override
    public Mono<Author> updateAuthor(UUID id, Author author) {
        author.setId(id);
        return super.repository.save(toData(author))
                .map(this::toEntity);
    }

    @Override
    public Mono<Long> countAll() {
        return super.repository.countAll();
    }

    @Override
    public Mono<Long> countAllByNationality(String nationality) {
        return super.repository.countAllByNationality(queryValue(nationality));
    }

    private String queryValue(String value) {
        return "%" + value + "%";
    }
}
