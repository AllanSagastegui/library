package pe.ask.library.persistence.adapter;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import pe.ask.library.model.publisher.Publisher;
import pe.ask.library.persistence.entity.PublisherEntity;
import pe.ask.library.persistence.helper.ReactiveAdapterOperations;
import pe.ask.library.persistence.repository.IPublisherReactiveRepository;
import pe.ask.library.port.out.persistence.IPublisherRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class PublisherReactiveRepositoryAdapter extends ReactiveAdapterOperations<
        Publisher,
        PublisherEntity,
        UUID,
        IPublisherReactiveRepository
        > implements IPublisherRepository {

    public PublisherReactiveRepositoryAdapter(IPublisherReactiveRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Publisher.class));
    }

    @Override
    public Flux<Publisher> getAllPublishers(int page, int size) {
        return super.repository.findAllPaginated(page * size, size)
                .map(this::toEntity);
    }

    @Override
    public Mono<Publisher> getPublisherById(UUID publisherId) {
        return super.repository.findById(publisherId)
                .map(this::toEntity);
    }

    @Override
    public Flux<Publisher> getPublishersByName(int page, int size, String name) {
        return super.repository.findPublisherByNamePaginated(page * size, size, queryValue(name))
                .map(this::toEntity);
    }

    @Override
    public Flux<Publisher> getAllPublishersByCountry(int page, int size, String country) {
        return super.repository.findPublisherByCountryPaginated(page * size, size, queryValue(country))
                .map(this::toEntity);
    }

    @Override
    public Mono<Long> countAll() {
        return super.repository.countAll();
    }

    @Override
    public Mono<Long> countAllByName(String name) {
        return super.repository.countAllByName(queryValue(name));
    }

    @Override
    public Mono<Long> countAllByCountry(String country) {
        return super.repository.countAllByCountry(queryValue(country));
    }

    private String queryValue(String value) {
        return "%" + value + "%";
    }
}
