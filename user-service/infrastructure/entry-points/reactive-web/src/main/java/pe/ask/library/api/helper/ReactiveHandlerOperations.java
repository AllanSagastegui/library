package pe.ask.library.api.helper;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.ask.library.api.exception.UnexpectedException;
import pe.ask.library.api.utils.validation.CustomValidator;
import pe.ask.library.model.exception.BaseException;
import pe.ask.library.model.utils.Pageable;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.security.Principal;
import java.util.UUID;
import java.util.function.Function;

public abstract class ReactiveHandlerOperations<U> {

    protected final U useCase;
    protected final ObjectMapper mapper;
    protected final CustomValidator validator;

    protected ReactiveHandlerOperations(U useCase, ObjectMapper mapper, CustomValidator validator) {
        this.useCase = useCase;
        this.mapper = mapper;
        this.validator = validator;
    }

    protected <S, T> T map(S source, Class<T> targetClass) {
        return mapper.map(source, targetClass);
    }

    protected <B> Mono<B> extractBody(ServerRequest request, Class<B> clazz) {
        return request.bodyToMono(clazz)
                .flatMap(validator::validate);
    }

    protected <B, D> Mono<D> extractBodyToDomain(ServerRequest request, Class<B> requestDtoClass, Class<D> domainClass) {
        return request.bodyToMono(requestDtoClass)
                .flatMap(validator::validate)
                .map(dto -> map(dto, domainClass));
    }

    protected <T> Mono<ServerResponse> buildResponse(Mono<T> pipeline) {
        return buildResponse(pipeline, HttpStatus.OK);
    }

    protected <T> Mono<ServerResponse> buildResponse(Mono<T> pipeline, HttpStatus status) {
        return pipeline
                .flatMap(response -> ServerResponse.status(status)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(this::handleError);
    }

    protected <T> Mono<ServerResponse> buildCreatedResponse(T body, URI location) {
        return ServerResponse.created(location)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .onErrorResume(this::handleError);
    }

    protected Mono<ServerResponse> buildNoContentResponse(Mono<Void> pipeline) {
        return pipeline
                .then(ServerResponse.noContent().build())
                .onErrorResume(this::handleError);
    }

    protected <D, R> Mono<ServerResponse> buildPagedResponse(
            Mono<Pageable<D>> pipeline,
            Class<R> responseClass) {

        return pipeline
                .map(pageable -> Pageable.<R>builder()
                        .page(pageable.getPage())
                        .size(pageable.getSize())
                        .totalElements(pageable.getTotalElements())
                        .totalPages(pageable.getTotalPages())
                        .content(pageable.getContent().stream()
                                .map(domain -> map(domain, responseClass))
                                .toList())
                        .build())
                .flatMap(response -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(response))
                .onErrorResume(this::handleError);
    }

    protected Mono<ServerResponse> handleError(Throwable ex) {
        return Mono.error(ex instanceof BaseException ? ex : new UnexpectedException(ex));
    }

    protected int getPage(ServerRequest request) {
        return getQueryParam(request, "page", Integer::parseInt, 0);
    }

    protected int getSize(ServerRequest request) {
        return getQueryParam(request, "size", Integer::parseInt, 10);
    }

    protected String getPathVar(ServerRequest request, String name) {
        return request.pathVariable(name);
    }

    protected UUID getPathUuid(ServerRequest request, String name) {
        return UUID.fromString(request.pathVariable(name));
    }

    protected String getQueryParam(ServerRequest request, String name) {
        return request.queryParam(name).orElse(null);
    }

    protected <T> T getQueryParam(ServerRequest request, String name, Function<String, T> converter, T defaultValue) {
        return request.queryParam(name)
                .map(converter)
                .orElse(defaultValue);
    }

    protected String getHeader(ServerRequest request, String headerName) {
        return request.headers().firstHeader(headerName);
    }

    protected URI createUri(ServerRequest request, String pathResult, Object... args) {
        return request.uriBuilder().path(pathResult).build(args);
    }

    protected Mono<String> getPrincipalName(ServerRequest request) {
        return request.principal().map(Principal::getName);
    }
}